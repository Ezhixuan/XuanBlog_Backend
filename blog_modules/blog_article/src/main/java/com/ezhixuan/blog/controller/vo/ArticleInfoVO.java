package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 文章详情 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ArticleInfoVO extends ArticlePageVO {

  @Schema(description = "文章内容")
  private String content;

  public ArticleInfoVO(ArticlePageVO pageVO, String content) {
    this.content = content;
    this.setId(pageVO.getId());
    this.setTitle(pageVO.getTitle());
    this.setSummary(pageVO.getSummary());
    this.setCover(pageVO.getCover());
    this.setWordCount(pageVO.getWordCount());
    this.setViewCount(pageVO.getViewCount());
    this.setLikeCount(pageVO.getLikeCount());
    this.setProjectId(pageVO.getProjectId());
    this.setStatus(pageVO.getStatus());
    this.setCreateTime(pageVO.getCreateTime());
    this.setUpdateTime(pageVO.getUpdateTime());
    this.setCategoryId(pageVO.getCategoryId());
    this.setCategoryName(pageVO.getCategoryName());
    this.setTagMap(pageVO.getTagMap());
  }
}
