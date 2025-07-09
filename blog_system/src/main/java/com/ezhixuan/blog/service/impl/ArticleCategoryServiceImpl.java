package com.ezhixuan.blog.service.impl;

import static java.util.Objects.isNull;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.mapper.ArticleCategoryMapper;
import com.ezhixuan.blog.service.ArticleCategoryService;

/**
* @author ezhixuan
* @description 针对表【article_category(分类表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:26
*/
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements ArticleCategoryService{

    private static final String DEFAULT_CATEGORY = "默认分类";

    /**
     * 获取默认 category id
     *
     * @return 默认category id
     * @author Ezhixuan
     */
    @Override
    @Cache
    public Long getDefaultId() {
        ArticleCategory defaultCategory = getOne(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getName,DEFAULT_CATEGORY));
        if (isNull(defaultCategory)) {
            defaultCategory = new ArticleCategory();
            defaultCategory.setName(DEFAULT_CATEGORY);
            save(defaultCategory);
        }
        return defaultCategory.getId();
    }
}




