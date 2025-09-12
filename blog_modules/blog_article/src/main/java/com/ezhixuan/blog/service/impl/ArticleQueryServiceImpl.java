package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.controller.vo.ArticleInfoVO;
import com.ezhixuan.blog.controller.vo.ArticlePageVO;
import com.ezhixuan.blog.entity.*;
import com.ezhixuan.blog.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 文章查询服务实现类
 *
 * @version 0.0.2beta
 * @author Ezhixuan
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleQueryServiceImpl implements ArticleQueryService {

  // 依赖注入各种服务
  private final ArticleService articleService;
  private final ArticleCategoryService categoryService;
  private final ArticleTagService tagService;
  private final ArticleContentService contentService;
  private final ArticleThumbService thumbService;
  private final ArticleOperateService operateService;
  private final LinkArticleCategoryService linkArticleCategoryService;
  private final LinkArticleTagService linkArticleTagService;

  /**
   * 根据查询条件分页查询文章列表
   *
   * @param articleQueryDTO 查询条件DTO
   * @return 分页结果
   */
  @Override
  public IPage<ArticlePageVO> pageListByDTO(ArticleQueryDTO articleQueryDTO) {
    // 处理标签和分类的ID过滤条件
    if (dealWithIds(articleQueryDTO)) {
      // 如果处理后没有匹配的文章ID，则返回空分页结果
      if (isEmpty(articleQueryDTO.getIds())) {
        return articleQueryDTO.toPage();
      }
    }

    // 执行文章分页查询
    IPage<Article> paged = articleService.pageList(articleQueryDTO);
    // 构造返回的分页结果
    IPage<ArticlePageVO> resultPage =
        new Page<>(paged.getCurrent(), paged.getSize(), paged.getTotal());
    resultPage.setRecords(convert(paged.getRecords()));
    return resultPage;
  }

  /**
   * 处理标签和分类ID查询条件，获取匹配的文章ID列表
   *
   * @param articleQueryDTO 查询条件
   * @return 是否需要根据ID进行过滤
   */
  private boolean dealWithIds(ArticleQueryDTO articleQueryDTO) {
    boolean hasId = Boolean.FALSE;
    // 根据标签ID查询文章ID
    if (!isEmpty(articleQueryDTO.getTagIds())) {
      Collection<Long> articleIds =
          linkArticleTagService.queryArticleId(articleQueryDTO.getTagIds());
      articleQueryDTO.getIds().addAll(articleIds);
      hasId = Boolean.TRUE;
    }
    // 根据分类ID查询文章ID
    if (!isEmpty(articleQueryDTO.getCategoryIds())) {
      Collection<Long> articleIds =
          linkArticleCategoryService.queryArticleId(articleQueryDTO.getCategoryIds());
      articleQueryDTO.getIds().addAll(articleIds);
      hasId = Boolean.TRUE;
    }
    return hasId;
  }

  /**
   * 将Article转换为ArticlePageVO
   *
   * @param article 文章
   * @return ArticlePageVO
   */
  private ArticlePageVO convert(Article article) {
    return convert(Collections.singletonList(article)).getFirst();
  }

  /**
   * 批量转换ArticleArticlePageVO
   *
   * @param collection 文章分页DTO集合
   * @return ArticlePageVO列表
   */
  private List<ArticlePageVO> convert(Collection<Article> collection) {
    if (ObjectUtils.isEmpty(collection)) {
      return Collections.emptyList();
    }

    List<ArticlePageVO> pageVOList = collection.stream().map(this::buildTempArticleVo).toList();
    List<Long> articleIds = collection.stream().map(Article::getId).toList();
    // 关联标签信息
    linkTag(pageVOList, articleIds);
    // 关联分类信息
    linkCategory(pageVOList, articleIds);
    return pageVOList;
  }

  /**
   * 构建临时 articlePageVo 还需要补充分类和标签数据
   *
   * @param article 文章
   * @since 0.0.2beta
   * @return ArticlePageVO 临时对象
   */
  private ArticlePageVO buildTempArticleVo(Article article) {
    if (article == null) {
      return null;
    }
    ArticlePageVO articlePageVO = new ArticlePageVO();
    articlePageVO.setId(article.getId());
    articlePageVO.setTitle(article.getTitle());
    articlePageVO.setUserId(article.getUserId());
    articlePageVO.setSummary(article.getSummary());
    articlePageVO.setCover(article.getCover());
    articlePageVO.setWordCount(article.getWordCount());
    articlePageVO.setViewCount(article.getViewCount());
    articlePageVO.setLikeCount(article.getLikeCount());
    articlePageVO.setCommentCount(article.getCommentCount());
    articlePageVO.setStatus(article.getStatus());
    articlePageVO.setCreateTime(article.getCreateTime());
    articlePageVO.setUpdateTime(article.getUpdateTime());
    return articlePageVO;
  }

  /**
   * 为文章列表关联分类信息
   *
   * @param pageVOList 文章VO列表
   * @param articleIds 文章ID列表
   */
  private void linkCategory(List<ArticlePageVO> pageVOList, List<Long> articleIds) {
    // 查询文章和分类的关联关系
    List<LinkArticleCategory> linkArticleCategoryList =
        linkArticleCategoryService.queryLink(articleIds);
    if (isEmpty(linkArticleCategoryList)) {
      return;
    }

    // 获取分类ID并查询分类信息
    Collection<Long> categoryIds =
        linkArticleCategoryList.stream()
            .map(LinkArticleCategory::getCategoryId)
            .collect(Collectors.toSet());
    Map<Long, String> categoryMap =
        categoryService.listByIds(categoryIds).stream()
            .collect(Collectors.toMap(ArticleCategory::getId, ArticleCategory::getName));

    // 建立文章ID到分类ID的映射关系
    Map<Long, Long> groupByArticleId =
        linkArticleCategoryList.stream()
            .collect(
                Collectors.toMap(
                    LinkArticleCategory::getArticleId, LinkArticleCategory::getCategoryId));

    // 为每篇文章设置分类信息
    pageVOList.forEach(
        item -> {
          Optional.ofNullable(groupByArticleId.get(item.getId()))
              .ifPresent(
                  categoryId -> {
                    item.setCategoryId(categoryId);
                    item.setCategoryName(categoryMap.get(categoryId));
                  });
        });
  }

  /**
   * 为文章列表关联标签信息
   *
   * @param pageVOList 文章VO列表
   * @param articleIds 文章ID列表
   */
  private void linkTag(List<ArticlePageVO> pageVOList, List<Long> articleIds) {
    // 查询文章和标签的关联关系
    List<LinkArticleTag> linkArticleTagList = linkArticleTagService.queryLink(articleIds);
    if (isEmpty(linkArticleTagList)) {
      return;
    }

    // 获取标签ID并查询标签信息
    Collection<Long> tagIds =
        linkArticleTagList.stream().map(LinkArticleTag::getTagId).collect(Collectors.toSet());
    Map<Long, String> tagMap =
        tagService.listByIds(tagIds).stream()
            .collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));

    // 按文章ID分组标签关联关系
    Map<Long, List<LinkArticleTag>> groupByArticleId =
        linkArticleTagList.stream().collect(Collectors.groupingBy(LinkArticleTag::getArticleId));

    // 为每篇文章设置标签信息
    pageVOList.forEach(
        item -> {
          Optional.ofNullable(groupByArticleId.get(item.getId()))
              .ifPresent(
                  tags -> {
                    Set<Long> innerTagIds =
                        tags.stream().map(LinkArticleTag::getTagId).collect(Collectors.toSet());
                    HashMap<Long, String> innerTagMap = HashMap.newHashMap(innerTagIds.size());
                    innerTagIds.forEach(
                        innerTagId -> {
                          innerTagMap.put(innerTagId, tagMap.get(innerTagId));
                        });
                    item.setTagMap(innerTagMap);
                  });
        });
  }

  /**
   * 获取文章详情,会进行缓存
   *
   * @param articleId 文章 id
   * @return ArticleInfoVO
   */
  @Cache(key = RedisKeyConstant.ARTICLE_INFO_PRE_KEY + "#articleId")
  @Override
  public ArticleInfoVO getArticleInfo(long articleId) {
    Article article = articleService.getArticleById(articleId);
    if (ObjectUtils.isEmpty(article)) {
      return null;
    }
    ArticlePageVO articlePageVO = convert(article);
    ArticleContent articleContent = contentService.getById(articleId);
    ArticleInfoVO articleInfoVO = new ArticleInfoVO();
    BeanUtils.copyProperties(articlePageVO, articleInfoVO);
    articleInfoVO.setContent(articleContent.getContent());
    return articleInfoVO;
  }

  /**
   * 查询文章详情
   *
   * @param articleId 文章id
   * @return 文章内容vo
   */
  @Override
  public ArticleInfoVO getArticleInfoVO(long articleId) {
    // 通过AOP代理调用带缓存的方法获取文章信息
    ArticleInfoVO articleInfoVO =
        ((ArticleQueryService) AopContext.currentProxy()).getArticleInfo(articleId);
    if (ObjectUtils.isEmpty(articleInfoVO)) {
      return null;
    }
    // 增加浏览量计数
    Integer viewCount = articleInfoVO.getViewCount() + 1;
    articleInfoVO.setViewCount(viewCount);

    // 获取当天点赞数并更新到文章信息中
    int thumbToday = thumbService.getThumbToday(articleId);
    articleInfoVO.setLikeCount(articleInfoVO.getLikeCount() + thumbToday);

    // 异步更新数据库中的浏览量
    operateService.asyncUpdateViewCount(articleId, viewCount);
    return articleInfoVO;
  }
}
