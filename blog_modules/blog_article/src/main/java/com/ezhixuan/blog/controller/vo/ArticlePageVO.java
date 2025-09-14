package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/** 文章分页 vo */
@Data
@Schema(description = "文章分页VO")
public class ArticlePageVO {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "文章标题")
  private String title;

  @Schema(description = "文章摘要")
  private String summary;

  @Schema(description = "封面图片")
  private String cover;

  @Schema(description = "分类id")
  private Long categoryId;

  @Schema(description = "分类")
  private String categoryName;

  @Schema(description = "项目ID")
  private Long projectId;

  @Schema(description = "标签")
  private Map<Long, String> tagMap;

  @Schema(description = "文章字数")
  private Integer wordCount;

  @Schema(description = "浏览量")
  private Integer viewCount;

  @Schema(description = "点赞数")
  private Integer likeCount;

  @Schema(description = "状态：1-已发布，0-草稿")
  private Integer status;

  @Schema(description = "创建时间")
  private LocalDateTime createTime;

  @Schema(description = "更新时间")
  private LocalDateTime updateTime;
}
