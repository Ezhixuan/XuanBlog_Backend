package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.mapper.ArticleCategoryMapper;
import com.ezhixuan.xuanblog_backend.service.ArticleCategoryService;

/**
* @author ezhixuan
* @description 针对表【article_category(分类表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:26
*/
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements ArticleCategoryService{

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

    private LambdaQueryWrapper<ArticleCategory> queryWrapper(String categoryName) {
        return new LambdaQueryWrapper<ArticleCategory>()
                .like(ArticleCategory::getName, categoryName);
    }
}




