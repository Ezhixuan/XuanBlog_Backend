package com.ezhixuan.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezhixuan.blog.domain.entity.article.LinkArticleTag;
import com.ezhixuan.blog.domain.vo.CountVO;

/**
* @author ezhixuan
* @description 针对表【link_article_tag(关联 文章与标签)】的数据库操作Mapper
* @createDate 2025-07-06 14:59:36
* @Entity com.ezhixuan.blog.domain.entity.article.LinkArticleTag
*/
public interface LinkArticleTagMapper extends BaseMapper<LinkArticleTag> {

    @Select("select tag_id as id, count(*) as count from link_article_tag group by tag_id")
    List<CountVO> getTagCount();
}




