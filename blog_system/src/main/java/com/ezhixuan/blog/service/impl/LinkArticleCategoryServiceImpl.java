package com.ezhixuan.blog.service.impl;

import static java.util.Objects.isNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.article.LinkArticleCategory;
import com.ezhixuan.blog.domain.vo.CountVO;
import com.ezhixuan.blog.mapper.LinkArticleCategoryMapper;
import com.ezhixuan.blog.service.LinkArticleCategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ezhixuan
 * @description 针对表【link_article_category(关联 文章 分类)】的数据库操作Service实现
 * @createDate 2025-07-06 14:59:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkArticleCategoryServiceImpl extends ServiceImpl<LinkArticleCategoryMapper, LinkArticleCategory>
    implements LinkArticleCategoryService {

    /**
     * 建立连接
     *
     * @param articleId 文章 id
     * @param categoryId 分类 id
     * @author Ezhixuan
     */
    @Override
    public void save(Long articleId, Long categoryId) {
        if (isNull(articleId) || isNull(categoryId)) {
            log.error("文章 id 分类 id 不能为空, articleId = {}, categoryId = {}", articleId, categoryId);
            throw new IllegalArgumentException("文章 id 分类 id 不能为空");
        }
        Optional.ofNullable(
                    getOne(Wrappers.<LinkArticleCategory>lambdaQuery()
                            .eq(LinkArticleCategory::getArticleId, articleId)
                            .eq(LinkArticleCategory::getCategoryId, categoryId)))
                .ifPresent(this::removeById);
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
    public List<CountVO> queryCategoryCount() {
        return baseMapper.getCategoryCount();
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
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<LinkArticleCategory>lambdaQuery().in(LinkArticleCategory::getArticleId, articleIds));
    }

    /**
     * 通过articleId 断开连接
     *
     * @param articleId 文章 id
     * @author Ezhixuan
     */
    @Override
    public void removeByArticleId(Long articleId) {
        remove(Wrappers.<LinkArticleCategory>lambdaQuery().eq(LinkArticleCategory::getArticleId, articleId));
    }

    /**
     * 通过 categoryId 断开连接
     *
     * @param categoryId 分类 id
     * @author Ezhixuan
     */
    @Override
    public void removeByCategoryId(Long categoryId) {
        remove(Wrappers.<LinkArticleCategory>lambdaQuery().eq(LinkArticleCategory::getCategoryId, categoryId));
    }
}
