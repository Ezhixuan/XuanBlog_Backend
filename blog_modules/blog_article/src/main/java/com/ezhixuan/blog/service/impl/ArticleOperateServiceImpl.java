package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.controller.dto.ArticleRecommendDTO;
import com.ezhixuan.blog.controller.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.Article;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.SystemException;
import com.ezhixuan.blog.service.*;
import com.ezhixuan.blog.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * 对 article 进增删改操作实现类
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Slf4j
@Service
public class ArticleOperateServiceImpl implements ArticleOperateService {

  @Resource private ArticleService articleService;
  @Resource private ArticleTagService articleTagService;
  @Resource private ArticleContentService articleContentService;
  @Resource private CategoryService categoryService;
  @Resource private TagService tagService;
  @Resource private RedisUtil redisUtil;

  /**
   * 上传文章
   *
   * @param submitDTO 提交 dto
   * @return Long 文章 id
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long submitArticle(ArticleSubmitDTO submitDTO) {
    if (isNull(submitDTO)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交数据不能为空");
    }
    Article article = submitDTO.toEntity();
    String content = submitDTO.getContent();
    int wordCount = parseWordCount(content);
    article.setWordCount(wordCount);
    articleService.saveOrUpdate(article);
    Long articleId = article.getId();
    List<Long> tagIds = submitDTO.getTagIds();
    articleTagService.link(articleId, tagIds);
    articleContentService.link(articleId, content);

    Thread.startVirtualThread(
        () -> {
          redisUtil.cleanCache(
              RedisKeyConstant.COUNT_TAG_KEY,
              RedisKeyConstant.COUNT_CATEGORY_KEY,
              RedisKeyConstant.ARTICLE_INFO_PRE_KEY + articleId);
        });
    return articleId;
  }

  /**
   * 解析Markdown文本的字数 该方法通过去除Markdown格式标记来计算纯文本字数
   *
   * @param markdown 原始Markdown格式文本
   * @return 解析后纯文本的字符数
   */
  private int parseWordCount(String markdown) {
    // 去除代码块
    markdown = markdown.replaceAll("(?s)```.*?```", "");
    // 去除行内代码
    markdown = markdown.replaceAll("`[^`]*`", "");
    // 去除图片
    markdown = markdown.replaceAll("!\\[.*?]\\(.*?\\)", "");
    // 去除链接
    markdown = markdown.replaceAll("\\[([^]]+)]\\([^)]+\\)", "$1");
    // 去除标题符号
    markdown = markdown.replaceAll("(?m)^#{1,6}\\s*", "");
    // 去除加粗、斜体
    markdown = markdown.replaceAll("[*_]{1,2}([^*_]+)[*_]{1,2}", "$1");
    // 去除引用符号
    markdown = markdown.replaceAll("(?m)^>\\s*", "");
    // 去除列表符号
    markdown = markdown.replaceAll("(?m)^[-*+]\\s+", "");
    markdown = markdown.replaceAll("(?m)^\\d+\\.\\s+", "");
    // 去除 HTML 标签
    markdown = markdown.replaceAll("<[^>]+>", "");
    markdown = markdown.trim();
    return markdown.replaceAll("\\s+", "").length();
  }

  /**
   * 删除文章
   *
   * @param articleId 文章 id
   * @return Boolean 是否成功
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean deleteArticleById(Long articleId) {
    if (isNull(articleId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
    }
    try {
      articleContentService.unLink(articleId);
      articleTagService.unLinkByArticleId(articleId);
    } catch (Exception exception) {
      log.error("解绑失败{}", articleId);
      throw new SystemException(ErrorCode.SYSTEM_ERROR);
    } finally {
      articleService.removeById(articleId);
    }
    return true;
  }

  /**
   * 删除分类
   *
   * @param categoryId 分类 id
   * @return Boolean 是否成功
   */
  @Override
  public boolean deleteCategoryById(Long categoryId) {
    if (isNull(categoryId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类不存在");
    }
    List<Article> articleList =
        articleService.list(Wrappers.<Article>lambdaQuery().eq(Article::getCategoryId, categoryId));
    if (!CollectionUtils.isEmpty(articleList)) {
      Thread.startVirtualThread(
          () -> {
            Long defaultCategoryId = categoryService.getDefaultId();
            articleList.forEach(article -> article.setCategoryId(defaultCategoryId));
            articleService.updateBatchById(articleList);
          });
    }
    return categoryService.removeById(categoryId);
  }

  /**
   * 删除标签
   *
   * @param tagId 标签 id
   * @return Boolean 是否成功
   */
  @Override
  public boolean deleteTagById(Long tagId) {
    if (isNull(tagId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不存在");
    }
    articleTagService.unLinkByTagId(tagId);
    return tagService.removeById(tagId);
  }

  /**
   * 更新文章浏览量
   *
   * @param articleId 文章 id
   * @param viewCount 浏览量
   */
  @Override
  public void updateViewCount(Long articleId, Integer viewCount) {
    if (isNull(articleId) || isNull(viewCount)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
    }
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
   * 设置推荐文章
   *
   * @param recommendDTO 推荐文章 dto
   */
  @Override
  public void setRecommendArticle(ArticleRecommendDTO recommendDTO) {
    Long recommendId = recommendDTO.getRecommendId();
    Long unRecommendId = recommendDTO.getUnRecommendId();
    // 从Redis中获取当前推荐文章列表
    Object recommendIdsObj = redisUtil.get(RedisKeyConstant.ARTICLE_RECOMMEND_LIST_KEY);

    // 初始化推荐文章ID列表
    List<Long> recommendIds;
    if (Objects.nonNull(recommendIdsObj) && recommendIdsObj instanceof List<?>) {
      recommendIds = (List<Long>) recommendIdsObj;
    } else {
      recommendIds = new ArrayList<>();
    }

    // 如果有需要取消推荐的文章ID，则从列表中移除
    if (Objects.nonNull(recommendDTO.getUnRecommendId())) {
      recommendIds.remove(recommendDTO.getUnRecommendId());
    }

    // 如果有需要推荐的文章ID，则添加到列表中
    if (Objects.nonNull(recommendId) && !recommendIds.contains(recommendId)) {
      recommendIds.add(recommendId);

      // 如果推荐文章数量超过5篇且没有指定取消推荐的文章，则移除一篇
      if (recommendIds.size() > 5 && isNull(unRecommendId)) {
        // 移除一篇（按ID排序后移除最小的ID）
        recommendIds.stream()
            .filter(Objects::nonNull)
            .min(Long::compareTo)
            .ifPresent(recommendIds::remove);
      }
    }

    // 将更新后的推荐文章列表保存到Redis中
    redisUtil.set(RedisKeyConstant.ARTICLE_RECOMMEND_LIST_KEY, recommendIds);
  }
}
