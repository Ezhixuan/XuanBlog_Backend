package com.ezhixuan.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezhixuan.blog.entity.Article;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【article(文章主表)】的数据库操作Mapper
 * @createDate 2025-09-13 10:40:23 @Entity com.ezhixuan.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

  @Select(
      "SELECT category_id, count(1) AS use_count FROM article GROUP BY category_id ORDER BY use_count DESC LIMIT #{num}")
  Map<Long, Long> selectCategoryUseCount(int num);
}
