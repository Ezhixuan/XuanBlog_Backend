package com.ezhixuan.blog.mapper;

import com.ezhixuan.blog.controller.article.LinkArticleCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezhixuan.blog.domain.vo.ArticleCategoryCountVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author ezhixuan
* @description 针对表【link_article_category(关联 文章 分类)】的数据库操作Mapper
* @createDate 2025-07-06 14:59:36
* @Entity com.ezhixuan.blog.controller.article.LinkArticleCategory
*/
public interface LinkArticleCategoryMapper extends BaseMapper<LinkArticleCategory> {

    @Select("SELECT category_id as id, count(*) as count FROM link_article_category GROUP BY category_id")
    List<ArticleCategoryCountVO> getCategoryCount();

}




