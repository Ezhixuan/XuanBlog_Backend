package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
* @author ezhixuan
* @description 针对表【article_category(分类表)】的数据库操作Service
* @createDate 2025-03-27 09:45:26
*/
public interface ArticleCategoryService extends IService<ArticleCategory> {

    /**
     * 根据分类名模糊搜索
     * @author Ezhixuan
     * @param categoryName 分类名
     * @return ids
     */
    Collection<Long> getIdsByCategoryName(String categoryName);

    /**
     * 获取默认 category id
     * @author Ezhixuan
     * @param
     * @return 默认category id
     */
    Long getDefaultId();
}
