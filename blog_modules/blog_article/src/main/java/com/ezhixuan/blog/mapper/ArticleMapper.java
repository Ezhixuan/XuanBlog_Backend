package com.ezhixuan.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezhixuan.blog.domain.dto.CountDto;
import com.ezhixuan.blog.domain.entity.Article;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【article(文章主表)】的数据库操作Mapper
 * @createDate 2025-09-13 10:40:23 @Entity com.ezhixuan.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

  @MapKey("entityId")
  @Select(
      "select category_id as entityId, count(1) as count from article group by entityId order by count desc limit #{num}")
  Map<Long, CountDto> selectCategoryUseCount(int num);
}
