package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;

/**
* @author ezhixuan
* @description 针对表【article_category(分类表)】的数据库操作Service
* @createDate 2025-03-27 09:45:26
*/
public interface ArticleCategoryService extends IService<ArticleCategory> {

    /**
     * 获取默认 category id
     * @author Ezhixuan
     * @param
     * @return 默认category id
     */
    Long getDefaultId();
}
