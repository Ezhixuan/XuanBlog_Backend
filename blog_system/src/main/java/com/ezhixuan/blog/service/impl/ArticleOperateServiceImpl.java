package com.ezhixuan.blog.service.impl;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.aop.CacheInterceptor;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.domain.entity.article.ArticleContent;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.*;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleOperateServiceImpl implements ArticleOperateService {

    private final ArticleService articleService;
    private final ArticleContentService contentService;
    private final ArticleTagService tagService;
    private final ArticleCategoryService categoryService;
    private final LinkArticleTagService linkArticleTagService;
    private final LinkArticleCategoryService linkArticleCategoryService;

    private final CacheInterceptor cacheInterceptor;

    /**
     * 上传博客
     *
     * @param articleSubmitDTO 提交dto
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO) {
        ThrowUtils.throwIf(StrUtil.isBlank(articleSubmitDTO.getTitle()), ErrorCode.PARAMS_ERROR, "博客标题不能为空");
        if (CollectionUtils.isEmpty(articleSubmitDTO.getTagIds())) {
            articleSubmitDTO.setTagIds(Collections.singletonList(tagService.getDefaultId()));
        }
        if (isNull(articleSubmitDTO.getCategoryId())) {
            articleSubmitDTO.setCategoryId(categoryService.getDefaultId());
        }

        long userId = StpUtil.getLoginIdAsLong();
        boolean hasId = false;
        if (Objects.nonNull(articleSubmitDTO.getId())) {
            boolean exists = articleService.lambdaQuery().eq(Article::getId, articleSubmitDTO.getId())
                .eq(Article::getUserId, userId).exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "文章不存在或无权操作");
            hasId = true;
        }

        Article article = BeanUtil.copyProperties(articleSubmitDTO, Article.class);
        article.setUserId(userId);

        articleService.saveOrUpdate(article);
        linkArticleTagService.saveAll(article.getId(), articleSubmitDTO.getTagIds());
        linkArticleCategoryService.save(article.getId(), articleSubmitDTO.getCategoryId());

        ArticleContent articleContent = new ArticleContent();
        articleContent.setArticleId(article.getId());
        articleContent.setContent(articleSubmitDTO.getContent());
        contentService.saveOrUpdate(articleContent);

        if (hasId) {
            Long articleId = article.getId();
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("Transaction committed. Cleaning cache for articleId: {}", articleId);
                    cacheInterceptor.cleanLocalCache(RedisKeyConstant.ARTICLE_INFO_PRE_KEY + articleId);
                }
            });
        }
    }

    /**
     * 异步执行更新操作
     *
     * @author Ezhixuan
     * @param articleId 文章 id
     * @param viewCount 查看次数
     */
    @Async
    @Override
    public void asyncUpdateViewCount(Long articleId, Integer viewCount) {
        requireIdNoNull(articleId);
        articleService.update(
            Wrappers.<Article>lambdaUpdate()
                    .eq(Article::getId, articleId)
                    .set(Article::getViewCount, viewCount)
        );
    }

    /**
     * 通过 articleId 删除文章
     *
     * @param articleId 文章 articleId
     * @return Boolean
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteArticleById(Long articleId) {
        requireIdNoNull(articleId);
        linkArticleCategoryService.removeByArticleId(articleId);
        linkArticleTagService.removeByArticleId(articleId);
        return articleService.removeById(articleId);
    }

    /**
     * 通过 categoryId 删除分类
     *
     * @param categoryId 分类 id
     * @return Boolean
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategoryById(Long categoryId) {
        requireIdNoNull(categoryId);
        linkArticleCategoryService.removeByCategoryId(categoryId);
        return categoryService.removeById(categoryId);
    }

    /**
     * 通过 tagId 删除标签
     *
     * @param tagId 标签 id
     * @return Boolean
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTagById(Long tagId) {
        requireIdNoNull(tagId);
        linkArticleTagService.removeByTagId(tagId);
        return tagService.removeById(tagId);
    }

    private <T> void requireIdNoNull(T id) {
        if (isNull(id)) {
            throw new IllegalArgumentException("id不能为空");
        }
    }
}
