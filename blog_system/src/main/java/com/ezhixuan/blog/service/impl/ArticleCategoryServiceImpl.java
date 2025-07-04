package com.ezhixuan.blog.service.impl;

import java.util.Collection;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
     * 根据分类名模糊搜索
     *
     * @param categoryName 分类名
     * @return ids
     * @author Ezhixuan
     */
    @Override
    public Collection<Long> getIdsByCategoryName(String categoryName) {
        LambdaQueryWrapper<ArticleCategory> qw = queryWrapper(categoryName);

        qw.select(ArticleCategory::getId);

        return listObjs(qw, o -> (long) o);
    }

    /**
     * 获取默认 category id
     *
     * @return 默认category id
     * @author Ezhixuan
     */
    @Override
    public Long getDefaultId() {
        ArticleCategory defaultCategory = getOne(queryWrapper(DEFAULT_CATEGORY));
        if (Objects.isNull(defaultCategory)) {
            defaultCategory = new ArticleCategory();
            defaultCategory.setName(DEFAULT_CATEGORY);
            defaultCategory.setDescription(DEFAULT_CATEGORY);
            save(defaultCategory);
        }
        return defaultCategory.getId();
    }

    private LambdaQueryWrapper<ArticleCategory> queryWrapper(String categoryName) {
        return new LambdaQueryWrapper<ArticleCategory>()
                .like(ArticleCategory::getName, categoryName);
    }
}




