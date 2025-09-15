package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.ArticleContent;

/**
 * @author ezhixuan
 * @description 针对表【article_content(文章内容（延迟加载）)】的数据库操作Service
 * @createDate 2025-09-13 10:40:23
 */
public interface ArticleContentService extends IService<ArticleContent> {

  /**
   * 将 content 与 article 建立连接
   *
   * @param articleId 文章 id
   * @param content 内容
   */
  void link(Long articleId, String content);

  /**
   * 删除文章内容
   *
   * @param articleId 文章 id
   */
  void unLink(Long articleId);

  /**
   * 根据文章 id 获取文章内容
   *
   * @param articleId 文章 id
   * @return 文章内容
   */
  String getContentByArticleId(Long articleId);
}
