package com.ezhixuan.blog.service.impl;

import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.article.LinkArticleCategory;
import com.ezhixuan.blog.domain.vo.ArticleCategoryCountVO;
import com.ezhixuan.blog.mapper.LinkArticleCategoryMapper;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.LinkArticleCategoryService;

import lombok.RequiredArgsConstructor;

/**
* @author ezhixuan
* @description 针对表【link_article_category(关联 文章 分类)】的数据库操作Service实现
* @createDate 2025-07-06 14:59:36
*/
@Service
@RequiredArgsConstructor
public class LinkArticleCategoryServiceImpl extends ServiceImpl<LinkArticleCategoryMapper, LinkArticleCategory>
    implements LinkArticleCategoryService{

    private final ArticleService articleService;

    /**
     * 建立连接
     *
     * @param articleId  文章 id
     * @param categoryId 分类 id
     * @author Ezhixuan
     */
    @Override
    public void save(Long articleId, Long categoryId) {
        LinkArticleCategory articleCategory = getOne(Wrappers.<LinkArticleCategory>lambdaQuery().eq(LinkArticleCategory::getArticleId, articleId)
                .eq(LinkArticleCategory::getCategoryId, categoryId));
        if (nonNull(articleCategory)) {
            removeById(articleCategory);
        }
        LinkArticleCategory link = new LinkArticleCategory();
        link.setArticleId(articleId);
        link.setCategoryId(categoryId);
        save(link);
    }

    /**
     * 获取关联文章 id
     *
     * @param categoryIds 分类 id
     * @return Collection<Long>
     * @author Ezhixuan
     */
    @Override
    public Collection<Long> queryArticleId(Collection<Long> categoryIds) {
        return listObjs(Wrappers.<LinkArticleCategory>lambdaQuery().select(LinkArticleCategory::getArticleId)
            .in(LinkArticleCategory::getCategoryId, categoryIds));
    }

    /**
     * 获取标签计数
     *
     * @return List<ArticleCategoryCountVO>
     * @author Ezhixuan
     */
    @Override
    public List<ArticleCategoryCountVO> getCategoryCount() {
        return baseMapper.getCategoryCount();
    }

    /**
     * 获取分类 id
     *
     * @param articleIds 文章 id
     * @return Collection<Long>
     * @author Ezhixuan
     */
    @Override
    public Collection<Long> queryCategoryId(List<Long> articleIds) {
        return listObjs(Wrappers.<LinkArticleCategory>lambdaQuery().select(LinkArticleCategory::getCategoryId)
            .in(LinkArticleCategory::getArticleId, articleIds));
    }

    /**
     * 获取关联信息
     *
     * @param articleIds 文章 id
     * @return List<LinkArticleCategory>
     * @author Ezhixuan
     */
    @Override
    public List<LinkArticleCategory> queryLink(List<Long> articleIds) {
        return list(Wrappers.<LinkArticleCategory>lambdaQuery().in(LinkArticleCategory::getArticleId, articleIds));
    }
}




