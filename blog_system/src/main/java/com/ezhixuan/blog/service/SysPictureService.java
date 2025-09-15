package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.picture.dto.PictureQueryDTO;
import com.ezhixuan.blog.controller.picture.dto.PictureUploadDTO;
import com.ezhixuan.blog.controller.picture.vo.PictureUploadVO;
import com.ezhixuan.blog.domain.entity.SysPicture;
import com.ezhixuan.blog.handler.oss.OssModelEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** 系统图片服务接口 */
public interface SysPictureService extends IService<SysPicture> {

  /**
   * 注册图片服务模型
   *
   * @param model 模型名称
   * @return boolean 是否注册成功
   */
  boolean register(String model);

  /**
   * 获取可用的OSS模型类型列表
   *
   * @return List<OssModelEnum> 可用的OSS模型枚举列表
   */
  List<OssModelEnum> getAvailableType();

  /**
   * 上传图片文件
   *
   * @param file 上传的图片文件
   * @param uploadDTO 图片上传参数
   * @return String 图片访问URL
   */
  String doUpload(MultipartFile file, PictureUploadDTO uploadDTO);

  /**
   * 获取图片列表
   *
   * @param queryDTO 查询参数
   * @return IPage<PictureUploadVO> 分页图片列表
   */
  IPage<PictureUploadVO> getPictureVOList(PictureQueryDTO queryDTO);

  /**
   * 删除指定URL的图片文件
   *
   * @param pictureUrl 图片文件的访问URL
   * @return 删除成功返回true，否则返回false
   */
  boolean delete(String pictureUrl);

  /**
   * 删除指定ID的图片文件
   *
   * @param unLivedPictureIds 不活跃的图片ID列表
   */
  void deleteById(List<Long> unLivedPictureIds);

  /**
   * 检查指定URL的图片是否已存在
   *
   * @param url 图片访问URL
   * @return boolean 如果URL已存在返回true，否则返回false
   */
  boolean existsByUrl(String url);
}
