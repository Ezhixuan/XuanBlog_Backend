package com.ezhixuan.blog.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用于修改排序
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class ProjectArticleDocArtDTO {

  @Schema(description = "文章 id")
  private Long articleId;

  @Schema(description = "排序")
  private Integer sortOrder;
}
