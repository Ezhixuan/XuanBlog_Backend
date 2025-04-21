package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ezhixuan.xuanblog_backend.aop.CacheInterceptor;
import com.ezhixuan.xuanblog_backend.domain.constant.RedisKeyConstant;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleContent;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleContentService;
import com.ezhixuan.xuanblog_backend.service.ArticleEditService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleEditServiceImpl implements ArticleEditService {

    final ArticleService articleService;
    final ArticleContentService contentService;
    final PlatformTransactionManager transactionManager;
    final CacheInterceptor cacheInterceptor;

    /**
     * 上传博客
     *
     * @param articleSubmitDTO 提交dto
     * @author Ezhixuan
     */
    @Override
    public void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO) {
        /*
         todo Ezhixuan : 1. 根据文章内容生成摘要
                         2. 提供根据文章标题自动搜索合适封面的选项
                         3. 字数计数
                         4. 内容审核
                         5. 支持将图片上传图床
                         6. 通过md文件上传文章
         */
        ThrowUtils.throwIf(StrUtil.isBlank(articleSubmitDTO.getTitle()), ErrorCode.PARAMS_ERROR, "博客标题不能为空");
        if (StrUtil.isBlank(articleSubmitDTO.getTagIds())) {
            articleSubmitDTO.setTagIds("1");
        }
        if (Objects.isNull(articleSubmitDTO.getCategoryId())) {
            articleSubmitDTO.setCategoryId(1L);
        }
        long userId = StpUtil.getLoginIdAsLong();
        if (Objects.nonNull(articleSubmitDTO.getId())) {
            boolean exists = articleService.lambdaQuery().eq(Article::getId, articleSubmitDTO.getId()).eq(Article::getUserId, userId).exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR);
        }
        Article article = BeanUtil.copyProperties(articleSubmitDTO, Article.class);
        article.setUserId(userId);
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            articleService.saveOrUpdate(article);
            ArticleContent articleContent = new ArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setContent(articleSubmitDTO.getContent());
            contentService.saveOrUpdate(articleContent);
            transactionManager.commit(transaction);
            cacheInterceptor.cleanLocalCache(RedisKeyConstant.ARTICLE_INFO_PRE_KEY + article.getId());
        }catch (Exception e) {
            log.error("事务回滚 {}", e.getMessage());
            transactionManager.rollback(transaction);
            throw e;
        }
    }
}
