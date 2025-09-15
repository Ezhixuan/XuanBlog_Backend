package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.Category;
import java.util.List;
import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【category(文章分类)】的数据库操作Service
 * @createDate 2025-09-13 10:40:23
 */
public interface CategoryService extends IService<Category> {

  /**
   * 获取分类的默认 id
   *
   * @return Long 分类的默认 id
   */
  Long getDefaultId();

  /**
   * 获取文章和分类关联
   *
   * @param articleIds 文章 id 列表
   * @return Map<Long, String> 分类 id 和分类名称的映射
   */
  Map<Long, String> getLink(List<Long> articleIds);
}
