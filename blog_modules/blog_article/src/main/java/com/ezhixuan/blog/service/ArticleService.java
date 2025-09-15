package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.Article;
import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【article(文章主表)】的数据库操作Service
 * @createDate 2025-09-13 10:40:23
 */
public interface ArticleService extends IService<Article> {

  /**
   * 分页查询
   *
   * @param articleQueryDTO 查询条件
   * @return 分页数据
   */
  IPage<Article> page(ArticleQueryDTO articleQueryDTO);

  /**
   * 获取文章分类使用数量
   *
   * @param num 数量
   * @return 分类使用数量
   */
  Map<Long, Long> getCategoryUseCount(int num);

  /**
   * 判断项目下是否有文章
   *
   * @param projectId 项目id
   * @return 是否有文章
   */
  boolean hasArticle(Long projectId);
}
