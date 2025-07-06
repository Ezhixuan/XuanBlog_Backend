package com.ezhixuan.blog.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.common.PageRequest;
import com.ezhixuan.blog.controller.article.LinkArticleCategory;
import com.ezhixuan.blog.controller.article.LinkArticleTag;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.dto.ArticlePageDTO;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.domain.entity.article.ArticleContent;
import com.ezhixuan.blog.domain.entity.article.ArticleTag;
import com.ezhixuan.blog.domain.vo.ArticleInfoVO;
import com.ezhixuan.blog.domain.vo.ArticlePageVO;
import com.ezhixuan.blog.service.*;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    private final ArticleService articleService;
    private final ArticleCategoryService categoryService;
    private final ArticleTagService tagService;
    private final ArticleContentService contentService;
    private final ArticleThumbService thumbService;
    private final LinkArticleCategoryService linkArticleCategoryService;
    private final LinkArticleTagService linkArticleTagService;

    @Override
    public IPage<ArticlePageVO> queryArticleListByDTO(ArticleQueryDTO articleQueryDTO) {
        // 特殊处理获取对应id
        if (!CollectionUtils.isEmpty(articleQueryDTO.getTagIds())) {
            articleQueryDTO.getIds().addAll(linkArticleTagService.queryArticleId(articleQueryDTO.getTagIds().stream().distinct().toList()));
        }
        if (!CollectionUtils.isEmpty(articleQueryDTO.getCategoryIds())) {
            articleQueryDTO.getIds().addAll(linkArticleCategoryService.queryArticleId(articleQueryDTO.getCategoryIds().stream().distinct().toList()));
        }
        IPage<ArticlePageDTO> paged = articleService.getArticlePageList(articleQueryDTO);
        return PageRequest.convert(paged, convert(paged.getRecords()));
    }

    private ArticlePageVO convert(ArticlePageDTO articlePageDTO) {
        return convert(Collections.singletonList(articlePageDTO)).getFirst();
    }

    private List<ArticlePageVO> convert(Collection<ArticlePageDTO> collection) {
        if (ObjectUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<Long> articleIds = collection.stream().map(ArticlePageDTO::getId).toList();
        Set<Long> categoryIdsAll = new HashSet<>(linkArticleCategoryService.queryCategoryId(articleIds));
        List<ArticlePageVO> pageVOList =
            collection.stream().map(item -> BeanUtil.copyProperties(item, ArticlePageVO.class)).toList();

        List<LinkArticleTag> linkArticleTagList = linkArticleTagService.queryLink(articleIds);

        if (!ObjectUtils.isEmpty(linkArticleTagList)) {
            Collection<Long> tagIds = linkArticleTagService.queryTagId(articleIds);
            Map<Long, String> tag =
                tagService.listByIds(tagIds).stream().collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));

            Map<Long, List<LinkArticleTag>> groupByArticleId =
                linkArticleTagList.stream().collect(Collectors.groupingBy(LinkArticleTag::getArticleId));

            pageVOList.forEach(item -> {
                List<Long> innerTagIds =
                    groupByArticleId.get(item.getId()).stream().map(LinkArticleTag::getTagId).toList();
                HashMap<Long, String> innerTagMap = HashMap.newHashMap(innerTagIds.size());
                for (long tagId : innerTagIds) {
                    innerTagMap.put(tagId, tag.get(tagId));
                }
                item.setTagMap(innerTagMap);
            });
        }

        // 补充category 如果存在
        if (!ObjectUtils.isEmpty(categoryIdsAll)) {
            Map<Long, String> category = categoryService.listByIds(categoryIdsAll).stream()
                .collect(Collectors.toMap(ArticleCategory::getId, ArticleCategory::getName));
            List<LinkArticleCategory> linkArticleCategoryList = linkArticleCategoryService.queryLink(articleIds);
            Map<Long, Long> categoryIdMap = linkArticleCategoryList.stream()
                .collect(Collectors.toMap(LinkArticleCategory::getArticleId, LinkArticleCategory::getCategoryId));
            pageVOList.forEach(item -> {
                Long categoryId = categoryIdMap.get(item.getId());
                item.setCategoryId(categoryId);
                item.setCategoryName(category.get(categoryId));
            });
        }

        return pageVOList;
    }

    /**
     * 获取文章详情,会进行缓存
     *
     * @author Ezhixuan
     * @param articleId 文章 id
     * @return ArticleInfoVO
     */
    @Cache(key = RedisKeyConstant.ARTICLE_INFO_PRE_KEY + "#articleId")
    @Override
    public ArticleInfoVO getArticleInfo(long articleId) {
        ArticlePageDTO articlePageDTO = articleService.getArticleById(articleId);
        if (ObjectUtils.isEmpty(articlePageDTO)) {
            return null;
        }
        ArticlePageVO articlePageVO = convert(articlePageDTO);
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
     * @author Ezhixuan
     */
    @Override
    public ArticleInfoVO getArticleInfoVO(long articleId) {
        ArticleInfoVO articleInfoVO = ((ArticleQueryService)AopContext.currentProxy()).getArticleInfo(articleId);
        if (ObjectUtils.isEmpty(articleInfoVO)) {
            return null;
        }
        Integer viewCount = articleInfoVO.getViewCount() + 1;
        articleInfoVO.setViewCount(viewCount);

        int thumbToday = thumbService.getThumbToday(articleId);
        articleInfoVO.setLikeCount(articleInfoVO.getLikeCount() + thumbToday);

        ((ArticleQueryService)AopContext.currentProxy()).asyncUpdateViewCount(articleId, viewCount);
        return articleInfoVO;
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
    public void asyncUpdateViewCount(long articleId, Integer viewCount) {
        articleService.update(
            Wrappers.<Article>lambdaUpdate().eq(Article::getId, articleId).set(Article::getViewCount, viewCount));
    }
}
