package com.ezhixuan.blog.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 推荐文章 DTO
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class ArticleRecommendDTO {

  @Schema(description = "推荐文章ID")
  private Long recommendId;

  @Schema(description = "取消推荐文章ID")
  private Long unRecommendId;
}
