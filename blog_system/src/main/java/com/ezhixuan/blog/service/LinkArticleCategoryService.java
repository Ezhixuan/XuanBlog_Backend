package com.ezhixuan.blog.service;

import com.ezhixuan.blog.controller.article.LinkArticleCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.vo.ArticleCategoryCountVO;

import java.util.Collection;
import java.util.List;

/**
* @author ezhixuan
* @description 针对表【link_article_category(关联 文章 分类)】的数据库操作Service
* @createDate 2025-07-06 14:59:36
*/
public interface LinkArticleCategoryService extends IService<LinkArticleCategory> {

    /**
     * 建立连接
     * @author Ezhixuan
     * @param articleId 文章 id
     * @param categoryId 分类 id
     */
    void save(Long articleId, Long categoryId);

    /**
     * 获取关联文章 id
     * @author Ezhixuan
     * @param categoryIds 分类 id
     * @return Collection<Long>
     */
    Collection<Long> queryArticleId(Collection<Long> categoryIds);

    /**
     * 获取标签计数
     * @author Ezhixuan
     * @return List<ArticleCategoryCountVO>
     */
    List<ArticleCategoryCountVO> getCategoryCount();

    /**
     * 获取分类 id
     * @author Ezhixuan
     * @param articleIds 文章 id
     * @return Collection<Long>
     */
    Collection<Long> queryCategoryId(List<Long> articleIds);

    /**
     * 获取关联信息
     * @author Ezhixuan
     * @param articleIds 文章 id
     * @return List<LinkArticleCategory>
     */
    List<LinkArticleCategory> queryLink(List<Long> articleIds);
}
