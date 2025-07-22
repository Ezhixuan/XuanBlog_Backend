package com.ezhixuan.blog.service.impl;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.common.PageRequest;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.dto.ArticlePageDTO;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.article.*;
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
    private final ArticleOperateService operateService;
    private final LinkArticleCategoryService linkArticleCategoryService;
    private final LinkArticleTagService linkArticleTagService;

    @Override
    public IPage<ArticlePageVO> pageListByDTO(ArticleQueryDTO articleQueryDTO) {
        // 特殊处理获取对应id
        if (dealWithIds(articleQueryDTO)) {
            if (isEmpty(articleQueryDTO.getIds())) {
                return articleQueryDTO.toIPage();
            }
        }

        IPage<ArticlePageDTO> paged = articleService.pageList(articleQueryDTO);
        return PageRequest.convert(paged, convert(paged.getRecords()));
    }

    private boolean dealWithIds(ArticleQueryDTO articleQueryDTO) {
        boolean hasId = Boolean.FALSE;
        if (!isEmpty(articleQueryDTO.getTagIds())) {
            articleQueryDTO.getIds().addAll(linkArticleTagService.queryArticleId(articleQueryDTO.getTagIds()));
            hasId = Boolean.TRUE;
        }
        if (!isEmpty(articleQueryDTO.getCategoryIds())) {
            articleQueryDTO.getIds().addAll(linkArticleCategoryService.queryArticleId(articleQueryDTO.getCategoryIds()));
            hasId = Boolean.TRUE;
        }
        return hasId;
    }

    private ArticlePageVO convert(ArticlePageDTO articlePageDTO) {
        return convert(Collections.singletonList(articlePageDTO)).getFirst();
    }

    private List<ArticlePageVO> convert(Collection<ArticlePageDTO> collection) {
        if (ObjectUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<ArticlePageVO> pageVOList = collection
                .stream()
                .map(item -> BeanUtil.copyProperties(item, ArticlePageVO.class))
                .toList();

        List<Long> articleIds = collection.stream().map(ArticlePageDTO::getId).toList();

        linkTag(pageVOList, articleIds);
        linkCategory(pageVOList, articleIds);
        return pageVOList;
    }

    private void linkCategory(List<ArticlePageVO> pageVOList, List<Long> articleIds) {
        List<LinkArticleCategory> linkArticleCategoryList = linkArticleCategoryService.queryLink(articleIds);
        if (isEmpty(linkArticleCategoryList)) {
            return;
        }
        Collection<Long> categoryIds = linkArticleCategoryList.stream().map(LinkArticleCategory::getCategoryId).collect(Collectors.toSet());
        Map<Long, String> categoryMap = categoryService.listByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(ArticleCategory::getId, ArticleCategory::getName));
        Map<Long, Long> groupByArticleId = linkArticleCategoryList.stream().collect(Collectors.toMap(LinkArticleCategory::getArticleId, LinkArticleCategory::getCategoryId));
        pageVOList.forEach(item -> {
            Optional.ofNullable(groupByArticleId.get(item.getId())).ifPresent(categoryId -> {
                item.setCategoryId(categoryId);
                item.setCategoryName(categoryMap.get(categoryId));
            });
        });
    }

    private void linkTag(List<ArticlePageVO> pageVOList, List<Long> articleIds) {
        List<LinkArticleTag> linkArticleTagList = linkArticleTagService.queryLink(articleIds);
        if (isEmpty(linkArticleTagList)) {
            return;
        }
        Collection<Long> tagIds = linkArticleTagList.stream().map(LinkArticleTag::getTagId).collect(Collectors.toSet());
        Map<Long, String> tagMap = tagService.listByIds(tagIds)
                .stream()
                .collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));

        Map<Long, List<LinkArticleTag>> groupByArticleId = linkArticleTagList
                .stream()
                .collect(Collectors.groupingBy(LinkArticleTag::getArticleId));

        pageVOList.forEach(item -> {
            Optional.ofNullable(groupByArticleId.get(item.getId())).ifPresent(tags -> {
                Set<Long> innerTagIds = tags.stream().map(LinkArticleTag::getTagId).collect(Collectors.toSet());
                HashMap<Long, String> innerTagMap = HashMap.newHashMap(innerTagIds.size());
                innerTagIds.forEach(innerTagId -> {
                    innerTagMap.put(innerTagId, tagMap.get(innerTagId));
                });
                item.setTagMap(innerTagMap);
            });
        });
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

        operateService.asyncUpdateViewCount(articleId, viewCount);
        return articleInfoVO;
    }
}
