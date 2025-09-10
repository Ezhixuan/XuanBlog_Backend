package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.mapper.ArticleCategoryMapper;
import com.ezhixuan.blog.service.ArticleCategoryService;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * 文章分类服务实现类
 *
 * @version 0.0.2beta
 * @author Ezhixuan
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements ArticleCategoryService {

  private static final String DEFAULT_CATEGORY = "默认分类";

  /**
   * 获取默认分类ID，如果不存在则创建默认分类
   *
   * @return Long 默认分类ID
   */
  @Cache
  @Override
  public Long getDefaultId() {
    // 查询默认分类
    ArticleCategory defaultCategory =
        getOne(
            Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getName, DEFAULT_CATEGORY));
    // 如果默认分类不存在，则创建一个默认分类
    if (isNull(defaultCategory)) {
      defaultCategory = new ArticleCategory();
      defaultCategory.setName(DEFAULT_CATEGORY);
      save(defaultCategory);
    }
    return defaultCategory.getId();
  }
}
