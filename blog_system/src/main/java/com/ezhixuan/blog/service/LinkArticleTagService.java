package com.ezhixuan.blog.service;

import com.ezhixuan.blog.controller.article.LinkArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.vo.ArticleTagCountVO;

import java.util.Collection;
import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【link_article_tag(关联 文章与标签)】的数据库操作Service
 * @createDate 2025-07-06 14:59:36
 */
public interface LinkArticleTagService extends IService<LinkArticleTag> {

    /**
     * 建立连接
     *
     * @author Ezhixuan
     * @param articleId 文章id
     * @param tagIds 标签id
     */
    void saveAll(Long articleId, String tagIds);

    /**
     * 获取关联的文章 id
     * @author Ezhixuan
     * @param tagIds 标签id
     * @return Collection<Long>
     */
    Collection<Long> queryArticleId(Collection<Long> tagIds);

    /**
     * 获取标签计数
     * @author Ezhixuan
     * @return List<ArticleTagCountVO>
     */
    List<ArticleTagCountVO> getTagCount();

    /**
     * 获取文章关联的标签 id
     * @author Ezhixuan
     * @param articleIds 文章id
     * @return Collection<Long>
     */
    Collection<Long> queryTagId(List<Long> articleIds);

    /**
     * 获取文章关联的标签
     * @author Ezhixuan
     * @param articleIds 文章id
     * @return List<LinkArticleTag>
     */
    List<LinkArticleTag> queryLink(List<Long> articleIds);
}
