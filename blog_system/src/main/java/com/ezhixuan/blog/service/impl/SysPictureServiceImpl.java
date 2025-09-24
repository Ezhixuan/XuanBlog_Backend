package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.picture.dto.PictureQueryDTO;
import com.ezhixuan.blog.controller.picture.dto.PictureUploadDTO;
import com.ezhixuan.blog.controller.picture.vo.PictureUploadVO;
import com.ezhixuan.blog.domain.entity.SysPicture;
import com.ezhixuan.blog.handler.oss.OssManager;
import com.ezhixuan.blog.handler.oss.OssModelEnum;
import com.ezhixuan.blog.mapper.SysPictureMapper;
import com.ezhixuan.blog.service.SysPictureService;
import com.ezhixuan.blog.utils.PictureCommonUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 系统图片服务实现类
 *
 * @author Ezhixuan
 */
@Service
public class SysPictureServiceImpl extends ServiceImpl<SysPictureMapper, SysPicture>
    implements SysPictureService {

  @Resource private OssManager ossManager;

  /**
   * 上传图片文件
   *
   * @param file      上传的图片文件
   * @param uploadDTO 图片上传参数
   * @return String 图片访问URL
   */
  @Override
  public PictureUploadVO doUpload(MultipartFile file, PictureUploadDTO uploadDTO) {
    long userId = StpUtil.getLoginIdAsLong();
    String targetPath =
        "public"
            + File.separator
            + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            + File.separator
            + String.format("%s", userId);
    String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("/");

    try {
      String name =
          uploadDTO.getReName()
              ? PictureCommonUtil.reName(file.getOriginalFilename())
              : split[split.length - 1];
      String uploadPath = targetPath + File.separator + name;
      String url = ossManager.getInstance().doUpload(file.getInputStream(), uploadPath);
      PictureUploadVO result = PictureUploadVO.builder().url(url).name(name).build();
      if (!existsByUrl(url)) {
          doUpload2Sys(userId, result, uploadDTO);
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取图片列表
   *
   * @param queryDTO 查询参数
   * @return IPage<PictureUploadVO> 分页图片列表
   */
  @Override
  public IPage<PictureUploadVO> getPictureVOList(PictureQueryDTO queryDTO) {
    LambdaQueryWrapper<SysPicture> queryWrapper =
        Wrappers.<SysPicture>lambdaQuery()
            .eq(SysPicture::getUserId, StpUtil.getLoginId())
            .orderByDesc(SysPicture::getUpdateTime);
    queryWrapper.eq(SysPicture::getType, queryDTO.getType());
    return page(queryDTO.toPage(), queryWrapper)
        .convert(item -> BeanUtil.copyProperties(item, PictureUploadVO.class));
  }

  @Override
  public boolean delete(String pictureUrl) {
    try {
      ossManager.doDelete(pictureUrl);
      remove(Wrappers.<SysPicture>lambdaQuery().eq(SysPicture::getUrl, pictureUrl));
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  /**
   * 通过 id 删除图片
   *
   * @param unLivedPictureIds 图片 id 列表
   */
  @Override
  public void deleteById(List<Long> unLivedPictureIds) {
    if (isEmpty(unLivedPictureIds)) {
      return;
    }
    List<SysPicture> pictureList = listByIds(unLivedPictureIds);
    if (isEmpty(pictureList)) {
      return;
    }
    pictureList.forEach(picture -> ossManager.doDelete(picture.getUrl()));
  }

  /**
   * 注册图片上传模型
   *
   * @param model 模型名称
   * @return boolean 是否注册成功
   */
  @Override
  public boolean register(String model) {
    return ossManager.register(model);
  }

  /**
   * 获取可用的上传类型列表
   *
   * @return List<UploadModel> 可用的上传类型列表
   */
  @Override
  public List<OssModelEnum> getAvailableType() {
    return ossManager.getAvailableType();
  }

  /**
   * 将上传的图片信息保存到系统数据库中
   *
   * @param userId 用户ID
   * @param uploadResult 上传结果
   * @param uploadDTO 上传参数
   */
  private void doUpload2Sys(long userId, PictureUploadVO uploadResult, PictureUploadDTO uploadDTO) {
    // 内部方法 传入的参数都经过验证 不需要对uploadResult进行二次验证
    SysPicture picture = BeanUtil.copyProperties(uploadResult, SysPicture.class);
    picture.setType(uploadDTO.getType());
    picture.setUserId(userId);
    Long picId = uploadDTO.getId();
    if (Objects.nonNull(picId)) {
      picture.setId(picId);
    }
    this.saveOrUpdate(picture);
    uploadResult.setId(picture.getId());
  }

  /**
   * 检查指定URL的图片是否已存在
   *
   * @param url 图片访问URL
   * @return boolean 如果URL已存在返回true，否则返回false
   */
  @Override
  public boolean existsByUrl(String url) {
    if (url == null || url.trim().isEmpty()) {
      return false;
    }
    return count(Wrappers.<SysPicture>lambdaQuery().eq(SysPicture::getUrl, url)) > 0;
  }
}
