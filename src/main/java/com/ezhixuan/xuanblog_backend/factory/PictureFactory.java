package com.ezhixuan.xuanblog_backend.factory;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ezhixuan.xuanblog_backend.configs.propertites.BlogUploadProp;
import com.ezhixuan.xuanblog_backend.configs.propertites.UploadModelEnum;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 图片工厂
 *
 * @author ezhixuan
 */
@Component
@RequiredArgsConstructor
public class PictureFactory {

    private final BlogUploadProp typeConfig;
    private final List<PictureManager> serviceList;

    private static UploadModelEnum type;

    @PostConstruct
    public void init() {
        type = UploadModelEnum.getUploadModelEnum(typeConfig.getType());
    }

    public PictureManager getInstance() {
        return serviceList.stream().filter(service -> Objects.equals(service.getUploadModelEnum(), type)).findAny()
            .orElseThrow(() -> new RuntimeException(ErrorCode.SYSTEM_ERROR.getMessage()));
    }
}
