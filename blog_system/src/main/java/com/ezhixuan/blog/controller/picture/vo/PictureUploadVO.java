package com.ezhixuan.blog.controller.picture.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PictureUploadVO {

    @Schema(description = "图片ID")
    private Long id;

    @Schema(description = "图片URL")
    private String url;

    @Schema(description = "图片名称")
    private String name;

    @Schema(description = "图片类型 1.博客内容图片 2.博客封面图片 3.博客用户头像")
    private Integer type;

}
