package com.ezhixuan.blog.service;

import com.ezhixuan.blog.controller.dto.ArticleRecommendDTO;
import com.ezhixuan.blog.controller.dto.ArticleSubmitDTO;

/**
 * 对 article 的增删改操作进行统一规范
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
public interface ArticleOperateService {

  /**
   * 上传文章
   *
   * @param submitDTO 提交 dto
   * @return Long 文章 id
   */
  Long submitArticle(ArticleSubmitDTO submitDTO);

  /**
   * 删除文章
   *
   * @param articleId 文章 id
   * @return Boolean 是否成功
   */
  boolean deleteArticleById(Long articleId);

  /**
   * 删除分类
   *
   * @param categoryId 分类 id
   * @return Boolean 是否成功
   */
  boolean deleteCategoryById(Long categoryId);

  /**
   * 删除标签
   *
   * @param tagId 标签 id
   * @return Boolean 是否成功
   */
  boolean deleteTagById(Long tagId);

  /**
   * 更新文章浏览量
   *
   * @param articleId 文章 id
   * @param viewCount 浏览量
   */
  void updateViewCount(Long articleId, Integer viewCount);

  /**
   * 设置推荐文章
   *
   * @param recommendDTO 推荐文章 dto
   */
  void setRecommendArticle(ArticleRecommendDTO recommendDTO);
}
