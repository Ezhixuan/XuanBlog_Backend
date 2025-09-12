package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.aop.CacheInterceptor;
import com.ezhixuan.blog.controller.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.entity.Article;
import com.ezhixuan.blog.entity.ArticleContent;
import com.ezhixuan.blog.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * 文章操作服务实现类
 *
 * @author Ezhixuan
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleOperateServiceImpl implements ArticleOperateService {

  private final ArticleService articleService;
  private final ArticleContentService contentService;
  private final ArticleTagService tagService;
  private final ArticleCategoryService categoryService;
  private final LinkArticleTagService linkArticleTagService;
  private final LinkArticleCategoryService linkArticleCategoryService;

  private final CacheInterceptor cacheInterceptor;

  /**
   * 提交文章（新增或更新）
   *
   * @param articleSubmitDTO 文章提交数据传输对象
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO) {
    if (!StringUtils.hasText(articleSubmitDTO.getTitle())) {
      throw new IllegalArgumentException("博客标题不能为空");
    }

    // 设置如果为空默认标签/分类
    if (CollectionUtils.isEmpty(articleSubmitDTO.getTagIds())) {
      articleSubmitDTO.setTagIds(Collections.singletonList(tagService.getDefaultId()));
    }
    if (isNull(articleSubmitDTO.getCategoryId())) {
      articleSubmitDTO.setCategoryId(categoryService.getDefaultId());
    }

    long userId = StpUtil.getLoginIdAsLong();
    if (Objects.nonNull(articleSubmitDTO.getId())) {
      boolean exists =
          articleService
              .lambdaQuery()
              .eq(Article::getId, articleSubmitDTO.getId())
              .eq(Article::getUserId, userId)
              .exists();
      if (!exists) {
        throw new IllegalArgumentException("文章不存在");
      }
    }

    submitArticle(articleSubmitDTO, userId);
  }

  /**
   * 删除指定ID的文章
   *
   * @param articleId 文章ID
   * @return Boolean 是否删除成功
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
   * 删除指定ID的分类
   *
   * @param categoryId 分类ID
   * @return Boolean 是否删除成功
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean deleteCategoryById(Long categoryId) {
    requireIdNoNull(categoryId);
    linkArticleCategoryService.removeByCategoryId(categoryId);
    return categoryService.removeById(categoryId);
  }

  /**
   * 删除指定ID的标签
   *
   * @param tagId 标签ID
   * @return Boolean 是否删除成功
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean deleteTagById(Long tagId) {
    requireIdNoNull(tagId);
    linkArticleTagService.removeByTagId(tagId);
    return tagService.removeById(tagId);
  }

  /**
   * 异步更新文章浏览量
   *
   * @param articleId 文章ID
   * @param viewCount 浏览量
   */
  @Override
  public void asyncUpdateViewCount(Long articleId, Integer viewCount) {
    requireIdNoNull(articleId);
    Thread.ofVirtual()
        .start(
            () -> {
              articleService.update(
                  Wrappers.<Article>lambdaUpdate()
                      .eq(Article::getId, articleId)
                      .set(Article::getViewCount, viewCount));
            });
  }

  /**
   * 执行文章保存或更新的具体操作
   *
   * @param articleSubmitDTO 文章提交数据传输对象
   * @param userId 用户ID
   */
  public void submitArticle(ArticleSubmitDTO articleSubmitDTO, long userId) {
    Article article = BeanUtil.copyProperties(articleSubmitDTO, Article.class);
    article.setUserId(userId);
    articleService.saveOrUpdate(article);
    linkArticleTagService.saveAll(article.getId(), articleSubmitDTO.getTagIds());
    linkArticleCategoryService.save(article.getId(), articleSubmitDTO.getCategoryId());

    ArticleContent articleContent = new ArticleContent();
    articleContent.setArticleId(article.getId());
    articleContent.setContent(articleSubmitDTO.getContent());
    contentService.saveOrUpdate(articleContent);
    cacheInterceptor.cleanLocalCache(RedisKeyConstant.ARTICLE_INFO_PRE_KEY + article.getId());
  }

  /**
   * 检查ID是否为空，如果为空则抛出异常
   *
   * @param id ID值
   * @param <T> ID类型
   */
  private <T> void requireIdNoNull(T id) {
    if (isNull(id)) {
      throw new IllegalArgumentException("id不能为空");
    }
  }
}
