package com.ezhixuan.xuanblog_backend.service.impl;

import static com.ezhixuan.xuanblog_backend.exception.ThrowUtils.throwIf;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.dto.PictureUploadDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.sys.SysPicture;
import com.ezhixuan.xuanblog_backend.domain.vo.PictureUploadVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.factory.PictureFactory;
import com.ezhixuan.xuanblog_backend.mapper.SysPictureMapper;
import com.ezhixuan.xuanblog_backend.service.SysPictureService;
import com.ezhixuan.xuanblog_backend.utils.PictureCommonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

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
            String name = PictureCommonUtil.reName(file.getOriginalFilename());
            String url = factory.getInstance().doUpload(file.getInputStream(), targetPath, name);
            PictureUploadVO result = PictureUploadVO.builder().url(url).name(name).build();
            doUpload2Sys(userId, result, uploadDTO.getId());
            return url;
        } catch (IOException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private void doUpload2Sys(long userId, PictureUploadVO uploadResult, Long picId) {
        // 内部方法 传入的参数都经过验证 不需要对uploadResult进行二次验证
        SysPicture picture = BeanUtil.copyProperties(uploadResult, SysPicture.class);
        picture.setUserId(userId);
        if (Objects.nonNull(picId)) {
            picture.setId(picId);
        }
        boolean resultSave = this.saveOrUpdate(picture);
        throwIf(!resultSave, ErrorCode.OPERATION_ERROR, "图片上传失败");
    }

    @Override
    public List<PictureUploadVO> getPictureVOList() {
        return list(Wrappers.<SysPicture>lambdaQuery().eq(SysPicture::getUserId, StpUtil.getLoginId())
            .orderByDesc(SysPicture::getUpdateTime)).stream()
            .map(pic -> PictureUploadVO.builder().id(pic.getId()).name(pic.getName()).url(pic.getUrl()).build())
            .toList();
    }
}
