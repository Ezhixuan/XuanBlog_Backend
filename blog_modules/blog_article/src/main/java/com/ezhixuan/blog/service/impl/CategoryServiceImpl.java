package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.service.CategoryService;
import com.ezhixuan.blog.domain.entity.Category;
import com.ezhixuan.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【category(文章分类)】的数据库操作Service实现
 * @createDate 2025-09-13 10:40:23
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {
  private static final String DEFAULT_CATEGORY = "默认分类";

  /**
   * 获取默认分类ID，如果不存在则创建默认分类
   *
   * @return Long 默认分类ID
   */
  @Cache(key = RedisKeyConstant.DEFAULT_CATEGORY_ID_KEY)
  @Override
  public Long getDefaultId() {
    // 查询默认分类
    Category defaultCategory =
        getOne(Wrappers.<Category>lambdaQuery().eq(Category::getName, DEFAULT_CATEGORY));
    // 如果默认分类不存在，则创建一个默认分类
    if (isNull(defaultCategory)) {
      defaultCategory = new Category();
      defaultCategory.setName(DEFAULT_CATEGORY);
      save(defaultCategory);
    }
    return defaultCategory.getId();
  }

  /**
   * 获取文章和分类关联
   *
   * @param articleIds 文章 id 列表
   * @return Map<Long, String> 分类 id 和分类名称的映射
   */
  @Override
  public Map<Long, String> getLink(List<Long> articleIds) {
    if (isEmpty(articleIds)) {
      return Map.of();
    }
    List<Category> categories =
        list(Wrappers.<Category>lambdaQuery().in(Category::getId, articleIds));
    if (isEmpty(categories)) {
      return Map.of();
    }
    return categories.stream()
        .collect(Collectors.toMap(Category::getId, Category::getName));
  }
}
