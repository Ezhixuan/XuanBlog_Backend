package com.ezhixuan.blog.handler.picture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
 todo Ezhixuan : 占个坑后续看有没有需求
 */
@Data
public class PictureUploadDTO {

    private Long id;

    @Schema(description = "图片类型 1.博客内容图片 2.博客封面图片 3.博客用户头像")
    private Integer type = 1;
}
