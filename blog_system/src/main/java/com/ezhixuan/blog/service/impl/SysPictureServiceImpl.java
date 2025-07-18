package com.ezhixuan.blog.service.impl;

import static com.ezhixuan.blog.exception.ThrowUtils.throwIf;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.sys.SysPicture;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.handler.picture.*;
import com.ezhixuan.blog.mapper.SysPictureMapper;
import com.ezhixuan.blog.service.SysPictureService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysPictureServiceImpl extends ServiceImpl<SysPictureMapper, SysPicture> implements SysPictureService {

    final PictureFactory factory;

    @Override
    public String doUpload(MultipartFile file, PictureUploadDTO uploadDTO) {
        long userId = StpUtil.getLoginIdAsLong();
        String targetPath = String.format("public/%s/", userId);
        try {
            String name = uploadDTO.getReName() ? PictureCommonUtil.reName(file.getOriginalFilename()) : file.getOriginalFilename();
            String url = factory.getInstance().doUpload(file.getInputStream(), targetPath, name);
            PictureUploadVO result = PictureUploadVO.builder().url(url).name(name).build();
            doUpload2Sys(userId, result, uploadDTO);
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doUpload2Sys(long userId, PictureUploadVO uploadResult, PictureUploadDTO uploadDTO) {
        // 内部方法 传入的参数都经过验证 不需要对uploadResult进行二次验证
        SysPicture picture = BeanUtil.copyProperties(uploadResult, SysPicture.class);
        picture.setType(uploadDTO.getType());
        picture.setUserId(userId);
        Long picId = uploadDTO.getId();
        if (Objects.nonNull(picId)) {
            picture.setId(picId);
        }
        boolean resultSave = this.saveOrUpdate(picture);
        throwIf(!resultSave, ErrorCode.OPERATION_ERROR, "图片上传失败");
    }

    @Override
    public List<PictureUploadVO> getPictureVOList(Integer type) {
        LambdaQueryWrapper<SysPicture> queryWrapper = Wrappers.<SysPicture>lambdaQuery()
                .eq(SysPicture::getUserId, StpUtil.getLoginId())
                .orderByDesc(SysPicture::getUpdateTime);

        // 如果传入了type参数，则按类型过滤
        if (Objects.nonNull(type)) {
            queryWrapper.eq(SysPicture::getType, type);
        }

        return list(queryWrapper).stream()
                .map(pic -> PictureUploadVO.builder()
                        .id(pic.getId())
                        .name(pic.getName())
                        .url(pic.getUrl())
                        .type(pic.getType())
                        .build())
                .toList();
    }

    @Override
    public boolean register(String model) {
        return factory.register(model);
    }

    @Override
    public List<UploadModel> getAvailableType() {
        return factory.getAvailableType();
    }
}
