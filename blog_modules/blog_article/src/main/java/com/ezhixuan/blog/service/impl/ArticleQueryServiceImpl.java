package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.controller.vo.ArticleInfoVO;
import com.ezhixuan.blog.controller.vo.ArticlePageVO;
import com.ezhixuan.blog.controller.vo.CountVO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.SysPicture;
import com.ezhixuan.blog.domain.entity.Article;
import com.ezhixuan.blog.domain.entity.Category;
import com.ezhixuan.blog.domain.entity.Tag;
import com.ezhixuan.blog.service.*;
import com.ezhixuan.blog.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 文章查询接口实现
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Service
public class ArticleQueryServiceImpl implements ArticleQueryService {

  @Resource private ArticleService articleService;
  @Resource private ArticleContentService contentService;
  @Resource private ArticleTagService articleTagService;
  @Resource private CategoryService categoryService;
  @Resource private TagService tagService;
  @Resource private SysPictureService sysPictureService;

  @Resource private ArticleThumbService articleThumbService;
  @Resource private ArticleOperateService articleOperateService;
  @Resource private RedisUtil redisUtil;

  /**
   * 分页查询文章列表
   *
   * @param articleQueryDTO 查询条件DTO对象
   * @return 分页结果，包含ArticlePageVO对象列表
   */
  @Override
  public IPage<ArticlePageVO> pageListByDTO(ArticleQueryDTO articleQueryDTO) {
    // 参数校验，如果查询条件为空则返回空页面
    if (isNull(articleQueryDTO)) {
      return new Page<>();
    }

    // 如果查询条件中包含标签ID，则获取与这些标签关联的文章ID，并添加到查询条件中
    if (!isEmpty(articleQueryDTO.getTagIds())) {
      List<Long> articleIds = articleTagService.getLinkedArticleIds(articleQueryDTO.getTagIds());
      articleQueryDTO.getIds().addAll(articleIds);
    }

    // 调用文章服务进行分页查询
    IPage<Article> paged = articleService.page(articleQueryDTO);

    // 创建返回结果页面对象
    IPage<ArticlePageVO> resultPaged =
        new Page<>(paged.getCurrent(), paged.getSize(), paged.getTotal());

    // 将文章实体转换为页面VO对象并设置到结果中
    resultPaged.setRecords(convertToPageVo(paged.getRecords()));
    return resultPaged;
  }

  /**
   * 获取文章详情（带缓存）
   *
   * @param articleId 文章ID
   * @return ArticleInfoVO 文章详情VO对象
   */
  @Cache(key = RedisKeyConstant.ARTICLE_INFO_PRE_KEY + "#articleId", expireTime = 60 * 60 * 24)
  @Override
  public ArticleInfoVO getArticleCache(long articleId) {
    // 根据文章ID获取文章实体
    Article article = articleService.getById(articleId);
    if (isNull(article)) {
      // 如果文章不存在，返回空的VO对象
      return new ArticleInfoVO();
    }

    // 将文章实体转换为页面VO对象
    ArticlePageVO pageVO = convertToPageVo(article);

    // 补充文章内容
    String content = contentService.getContentByArticleId(articleId);

    // 构造并返回文章详情VO对象
    return new ArticleInfoVO(pageVO, content);
  }

  /**
   * 将单个文章实体转换为页面VO对象
   *
   * @param article 文章实体
   * @return ArticlePageVO 页面VO对象
   */
  private ArticlePageVO convertToPageVo(Article article) {
    return convertToPageVo(Collections.singletonList(article)).getFirst();
  }

  /**
   * 将文章实体列表转换为页面VO对象列表
   *
   * @param articleList 文章实体列表
   * @return ArticlePageVO 页面VO对象列表
   */
  private List<ArticlePageVO> convertToPageVo(List<Article> articleList) {
    // coverId => cover URL
    // categoryId => categoryName
    // articleId => tagMap

    // 如果文章列表为空，返回空列表
    if (isEmpty(articleList)) {
      return Collections.emptyList();
    }

    // 提取文章ID列表
    List<Long> articleIds = articleList.stream().map(Article::getId).toList();

    // 获取文章与标签的关联关系
    Map<Long, List<Tag>> tagLinked = articleTagService.getLink(articleIds);

    // 获取文章与分类的关联关系（分类ID到分类名称的映射）
    Map<Long, String> categoryLinked = categoryService.getLink(articleIds);

    // 提取文章封面ID列表
    List<Long> coverIds = articleList.stream().map(Article::getCoverId).toList();

    // 获取封面图片信息（ID到URL的映射）
    Map<Long, String> coverLinked =
        sysPictureService
            .list(Wrappers.<SysPicture>lambdaQuery().in(SysPicture::getId, coverIds))
            .stream()
            .collect(Collectors.toMap(SysPicture::getId, SysPicture::getUrl));

    // 将文章实体转换为VO对象
    return articleList.stream()
        .map(
            article -> {
              ArticlePageVO pageVO = new ArticlePageVO();

              // 设置标签映射（标签ID到标签名称）
              List<Tag> tags = tagLinked.get(article.getId());
              if (tags != null) {
                pageVO.setTagMap(tags.stream().collect(Collectors.toMap(Tag::getId, Tag::getName)));
              }

              // 设置文章基本信息
              pageVO.setId(article.getId());
              pageVO.setTitle(article.getTitle());
              pageVO.setSummary(article.getSummary());
              pageVO.setCover(coverLinked.get(article.getCoverId()));
              pageVO.setCategoryId(article.getCategoryId());
              pageVO.setCategoryName(categoryLinked.get(article.getCategoryId()));
              pageVO.setProjectId(article.getProjectId());
              pageVO.setWordCount(article.getWordCount());
              pageVO.setViewCount(article.getViewCount());
              pageVO.setLikeCount(article.getLikeCount());
              pageVO.setStatus(article.getStatus());
              pageVO.setCreateTime(article.getCreateTime());
              pageVO.setUpdateTime(article.getUpdateTime());
              return pageVO;
            })
        .toList();
  }

  /**
   * 查询文章详情（用于展示）
   *
   * @param articleId 文章ID
   * @return 文章内容VO对象
   */
  @Override
  public ArticleInfoVO getArticleVO(long articleId) {
    // 通过AOP代理调用带缓存的方法获取文章信息
    ArticleInfoVO articleInfoVO =
        ((ArticleQueryService) AopContext.currentProxy()).getArticleCache(articleId);

    // 增加浏览量计数
    Integer viewCount = articleInfoVO.getViewCount() + 1;
    articleInfoVO.setViewCount(viewCount);

    // 获取当天点赞数并更新到文章信息中
    int thumbToday = articleThumbService.getThumbToday(articleId);
    articleInfoVO.setLikeCount(articleInfoVO.getLikeCount() + thumbToday);

    // 异步更新数据库中的浏览量
    articleOperateService.updateViewCount(articleId, viewCount);
    return articleInfoVO;
  }

  /**
   * 分类统计
   *
   * @param num 统计数量
   * @return 分类统计
   */
  @Override
  public List<CountVO> getCategoryCountVo(int num) {
    // 获取分类使用数量
    Map<Long, Long> count = articleService.getCategoryUseCount(num);
    if (count.isEmpty()) {
      return Collections.emptyList();
    }

    // 获取分类名称
    Set<Long> categoryIds = count.keySet();
    List<Category> categoryList = categoryService.listByIds(categoryIds);
    if (categoryList.isEmpty()) {
      return Collections.emptyList();
    }
    Map<Long, String> categoryNameMap =
        categoryList.stream().collect(Collectors.toMap(Category::getId, Category::getName));

    return count.entrySet().stream()
        .map(
            entry -> {
              CountVO countVO = new CountVO();
              countVO.setId(entry.getKey());
              countVO.setCount(entry.getValue());
              countVO.setName(categoryNameMap.get(entry.getKey()));
              return countVO;
            })
        .toList();
  }

  /**
   * 标签统计
   *
   * @param num 统计数量
   * @return 标签统计
   */
  @Override
  public List<CountVO> getTagCountVo(int num) {
    Map<Long, Long> count = articleTagService.getTagUseCount(num);
    if (count.isEmpty()) {
      return Collections.emptyList();
    }
    // 获取标签名称
    Set<Long> tagIds = count.keySet();
    List<Tag> tagList = tagService.listByIds(tagIds);
    if (tagList.isEmpty()) {
      return Collections.emptyList();
    }
    Map<Long, String> tagNameMap =
        tagList.stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
    return count.entrySet().stream()
        .map(
            entry -> {
              CountVO countVO = new CountVO();
              countVO.setId(entry.getKey());
              countVO.setCount(entry.getValue());
              countVO.setName(tagNameMap.get(entry.getKey()));
              return countVO;
            })
        .toList();
  }

  /**
   * 获取推荐文章
   *
   * @param num 获取数量
   * @return 推荐文章
   */
  @Override
  public List<ArticlePageVO> getRecommendedList(int num) {
    Object recommendIdsObj = redisUtil.get(RedisKeyConstant.ARTICLE_RECOMMEND_LIST_KEY);
    ArticleQueryDTO articleQueryDTO = new ArticleQueryDTO();
    if (Objects.nonNull(recommendIdsObj) && recommendIdsObj instanceof List<?> recommendIds) {
      recommendIds.forEach(
          id -> {
            if (id instanceof Long) {
              articleQueryDTO.getIds().add((Long) id);
            }
          });
    } else {
      // 查询最新的有封面的 num 条数据
      articleQueryDTO.setPageSize(num);
      articleQueryDTO.setNeedCover(true);
    }
    return pageListByDTO(articleQueryDTO).getRecords();
  }
}
