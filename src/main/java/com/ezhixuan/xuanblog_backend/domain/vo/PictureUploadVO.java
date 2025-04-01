package com.ezhixuan.xuanblog_backend.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PictureUploadVO {

    /**
     * 图片地址
     */
    private String url;

    /**
     * 图片名称
     */
    private String name;

}
