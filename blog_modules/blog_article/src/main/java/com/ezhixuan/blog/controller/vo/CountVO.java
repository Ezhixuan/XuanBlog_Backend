package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CountVO {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "文章数")
  private Long count;

  @Schema(description = "分类名称")
  private String name;
}
