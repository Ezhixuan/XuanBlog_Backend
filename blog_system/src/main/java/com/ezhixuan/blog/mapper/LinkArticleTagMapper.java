package com.ezhixuan.blog.mapper;

import com.ezhixuan.blog.controller.article.LinkArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezhixuan.blog.domain.vo.ArticleTagCountVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author ezhixuan
* @description 针对表【link_article_tag(关联 文章与标签)】的数据库操作Mapper
* @createDate 2025-07-06 14:59:36
* @Entity com.ezhixuan.blog.controller.article.LinkArticleTag
*/
public interface LinkArticleTagMapper extends BaseMapper<LinkArticleTag> {

    @Select("select id,tag_id as id, count(*) as count from link_article_tag group by tag_id")
    List<ArticleTagCountVO> getTagCount();
}




