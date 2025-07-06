package com.ezhixuan.blog.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
    private final PlatformTransactionManager transactionManager;
    private final CacheInterceptor cacheInterceptor;
    private final ArticleTagService tagService;
    private final ArticleCategoryService categoryService;
    private final LinkArticleTagService linkArticleTagService;
    private final LinkArticleCategoryService linkArticleCategoryService;

    /**
     * 上传博客
     *
     * @param articleSubmitDTO 提交dto
     * @author Ezhixuan
     */
    @Override
    public void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO) {
        ThrowUtils.throwIf(StrUtil.isBlank(articleSubmitDTO.getTitle()), ErrorCode.PARAMS_ERROR, "博客标题不能为空");
        if (StrUtil.isBlank(articleSubmitDTO.getTagIds())) {
            articleSubmitDTO.setTagIds(tagService.getDefaultId().toString());
        }
        if (Objects.isNull(articleSubmitDTO.getCategoryId())) {
            articleSubmitDTO.setCategoryId(categoryService.getDefaultId());
        }

        long userId = StpUtil.getLoginIdAsLong();
        if (Objects.nonNull(articleSubmitDTO.getId())) {
            // 编辑态
            boolean exists = articleService.lambdaQuery().eq(Article::getId, articleSubmitDTO.getId())
                .eq(Article::getUserId, userId).exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR);
        }
        Article article = BeanUtil.copyProperties(articleSubmitDTO, Article.class);
        article.setUserId(userId);

        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            articleService.saveOrUpdate(article);
            linkArticleTagService.saveAll(article.getId(), articleSubmitDTO.getTagIds());
            linkArticleCategoryService.save(article.getId(), articleSubmitDTO.getCategoryId());
            ArticleContent articleContent = new ArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setContent(articleSubmitDTO.getContent());
            contentService.saveOrUpdate(articleContent);
            transactionManager.commit(transaction);
            cacheInterceptor.cleanLocalCache(RedisKeyConstant.ARTICLE_INFO_PRE_KEY + article.getId());
        } catch (Exception e) {
            log.error("事务回滚 {}", e.getMessage());
            transactionManager.rollback(transaction);
            throw e;
        }
    }
}
