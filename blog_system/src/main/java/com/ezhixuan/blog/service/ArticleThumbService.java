package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.article.ArticleThumb;
import java.util.Collection;

/**
 * 文章点赞服务接口
 *
 * @version 0.0.2beta
 * @author Ezhixuan
 */
public interface ArticleThumbService extends IService<ArticleThumb> {

  /**
   * 对文章进行点赞操作
   *
   * @param articleId 文章ID
   * @return boolean 点赞是否成功
   */
  boolean doThumb(Long articleId);

  /**
   * 获取文章当天的点赞数
   *
   * @param articleId 文章ID
   * @return int 当天点赞数
   */
  int getThumbToday(Long articleId);

  /**
   * 将文章点赞数据同步到Redis
   *
   * @param articleIds 文章ID集合
   */
  void syncToRedis(Collection<Long> articleIds);
}
