package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.dto.ArticleSubmitDTO;

public interface ArticleOperateService {

    /**
     * 上传博客
     * @author Ezhixuan
     * @param articleSubmitDTO 提交dto
     */
    void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO);

    /**
     * 异步执行更新操作
     *
     * @author Ezhixuan
     * @param articleId 文章 id
     * @param viewCount 查看次数
     */
    void asyncUpdateViewCount(Long articleId, Integer viewCount);

    /**
     * 通过 articleId 删除文章
     * @author Ezhixuan
     * @param articleId 文章 articleId
     * @return Boolean
     */
    Boolean deleteArticleById(Long articleId);

    /**
     * 通过 categoryId 删除分类
     * @author Ezhixuan
     * @param categoryId 分类 id
     * @return Boolean
     */
    Boolean deleteCategoryById(Long categoryId);

    /**
     * 通过 tagId 删除标签
     * @author Ezhixuan
     * @param tagId 标签 id
     * @return Boolean
     */
    Boolean deleteTagById(Long tagId);
}
