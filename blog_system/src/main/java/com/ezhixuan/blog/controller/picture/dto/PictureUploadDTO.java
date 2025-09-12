package com.ezhixuan.blog.controller.picture.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PictureUploadDTO {

  @Schema(description = "图片ID")
  private Long id;

  @Schema(description = "图片类型 1.博客内容图片 2.博客封面图片 3.博客用户头像")
  private Integer type = 1;

  @Schema(description = "是否重命名")
  private Boolean reName = true;
}
