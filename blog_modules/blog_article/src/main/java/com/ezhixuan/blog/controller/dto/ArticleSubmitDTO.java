package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 文章提交 dto */
@Schema(description = "文章提交DTO")
@Data
public class ArticleSubmitDTO {

  @Schema(description = "文章ID，如果存在则表示本次提交为文章更新")
  private Long id;

  @Schema(description = "文章标题")
  @NotNull
  private String title;

  @Schema(description = "文章摘要")
  private String summary;

  @Schema(description = "封面图片ID")
  private Long coverId;

  @Schema(description = "分类ID")
  private Long categoryId;

  @Schema(description = "标签ID列表")
  private List<Long> tagIds;

  @Schema(description = "状态：1-已发布，0-草稿")
  @NotNull
  private Integer status;

  @Schema(description = "文章内容")
  @NotNull
  private String content;

  @Schema(description = "字数统计")
  private Integer wordCount;

  @Schema(description = "项目ID")
  private Long projectId;

  public Article toEntity() {
    Article article = new Article();
    article.setId(id);
    article.setTitle(title);
    article.setCoverId(coverId);
    article.setSummary(summary);
    article.setCategoryId(categoryId);
    article.setProjectId(projectId);
    article.setStatus(status);
    article.setWordCount(wordCount);
    return article;
  }
}
