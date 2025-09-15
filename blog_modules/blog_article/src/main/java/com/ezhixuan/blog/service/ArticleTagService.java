package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.ArticleTag;
import com.ezhixuan.blog.domain.entity.Tag;
import java.util.List;
import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【article_tag(文章标签关联)】的数据库操作Service
 * @createDate 2025-09-13 10:40:23
 */
public interface ArticleTagService extends IService<ArticleTag> {

  /**
   * 将 tag 与 article 建立连接
   *
   * @param articleId 文章 id
   * @param tagIds 标签 id 列表
   */
  void link(Long articleId, List<Long> tagIds);

  /**
   * 删除文章标签关联
   *
   * @param articleId 文章 id
   */
  void unLinkByArticleId(Long articleId);

  /**
   * 删除文章标签关联
   *
   * @param tagId 标签 id
   */
  void unLinkByTagId(Long tagId);

  /**
   * 获取文章标签关联
   *
   * @param articleIds 文章 id 列表
   * @return Map<文章 id, 标签列表>
   */
  Map<Long, List<Tag>> getLink(List<Long> articleIds);

  /**
   * 获取标签关联的文章
   *
   * @param tagIds 标签 id 列表
   * @return List<文章 id>
   */
  List<Long> getLinkedArticleIds(List<Long> tagIds);

  /**
   * 获取标签使用数量
   *
   * @param num 数量
   * @return Map<标签 id, 使用数量>
   */
  Map<Long, Long> getTagUseCount(int num);
}
