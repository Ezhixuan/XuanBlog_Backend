package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.common.PageRequest;
import com.ezhixuan.xuanblog_backend.domain.constant.RedisKeyConstant;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticlePageDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleContent;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleCategoryCountVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleInfoVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleTagCountVO;
import com.ezhixuan.xuanblog_backend.service.*;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    final ArticleService articleService;
    final ArticleCategoryService categoryService;
    final ArticleTagService tagService;
    final ArticleContentService contentService;
    final ArticleThumbService thumbService;

    @Override
    public IPage<ArticlePageVO> getArticlePageVOList(ArticleQueryDTO articleQueryDTO) {
        String categoryName = articleQueryDTO.getCategoryName();
        String tagName = articleQueryDTO.getTagName();

        // 特殊处理获取对应id
        if (Objects.nonNull(tagName)) {
            articleQueryDTO.setTagIds(tagService.getIdsByTagName(articleQueryDTO.getTagName()));
        }
        if (Objects.nonNull(categoryName)) {
            articleQueryDTO.setCategoryIds(categoryService.getIdsByCategoryName(articleQueryDTO.getCategoryName()));
        }
        IPage<ArticlePageDTO> paged = articleService.getArticlePageList(articleQueryDTO);
        return PageRequest.convert(paged, toPageVOList(paged.getRecords()));
    }

    @Override
    public ArticlePageVO toPageVO(ArticlePageDTO articlePageDTO) {
        ArticlePageVO articlePageVO = BeanUtil.copyProperties(articlePageDTO, ArticlePageVO.class);

        // 处理
        if (!ObjectUtils.isEmpty(articlePageDTO.getTagIds())) {
            List<Long> tagIds =
                    Arrays.stream(articlePageDTO.getTagIds().split(",")).toList().stream().map(Long::parseLong).toList();
            Map<Long, String> tag =
                    tagService.listByIds(tagIds).stream().collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));
            articlePageVO.setTagMap(tag);
        }
        if (!Objects.isNull(articlePageDTO.getCategoryId())) {
            ArticleCategory categoryById = categoryService.getById(articlePageDTO.getCategoryId());
            articlePageVO.setCategoryId(articlePageDTO.getCategoryId());
            articlePageVO.setCategoryName(categoryById.getName());
        }
        return articlePageVO;
    }

    @Override
    public List<ArticlePageVO> toPageVOList(Collection<ArticlePageDTO> collection) {
        if (ObjectUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }

        Set<Long> categoryIdsAll = new HashSet<>();

        List<ArticlePageVO> pageVOList = collection.stream().map(item -> {
            // 预处理减少循环次数
            categoryIdsAll.add(item.getCategoryId());
            // 转化
            return BeanUtil.copyProperties(item, ArticlePageVO.class);
        }).toList();

        // 补充tag 如果存在
        Map<Long, List<Long>> tagMap = collection.stream().collect(Collectors.toMap(ArticlePageDTO::getId,
            item -> Arrays.stream(item.getTagIds().split(",")).toList().stream().map(Long::parseLong).toList()));

        if (!ObjectUtils.isEmpty(tagMap)) {
            Set<Long> tagIds = tagMap.values().stream().flatMap(List::stream).collect(Collectors.toSet());

            Map<Long, String> tag =
                tagService.listByIds(tagIds).stream().collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));
            pageVOList.forEach(item -> {
                List<Long> innerTagIds = tagMap.get(item.getId());
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
            pageVOList.forEach(item -> {
                item.setCategoryName(category.get(item.getCategoryId()));
            });
        }

        return pageVOList;
    }

    /**
     * 菜单数量使用情况统计
     *
     * @return 统计列表
     * @author Ezhixuan
     */
    @Override
    public List<ArticleCategoryCountVO> getCategoryCount() {
        List<Long> categoryList =
            articleService.listObjs(Wrappers.<Article>lambdaQuery().select(Article::getCategoryId), o -> (Long)o);
        if (ObjectUtils.isEmpty(categoryList))
            return new ArrayList<>();
        Map<Long, Integer> count = categoryList.stream().collect(Collectors.toMap(id -> id, id -> 1, Integer::sum));

        Map<Long, ArticleCategory> categoryMap = categoryService.listByIds(count.keySet()).stream()
            .collect(Collectors.toMap(ArticleCategory::getId,t -> t));
        List<ArticleCategoryCountVO> cateCountVOList = new ArrayList<>(categoryMap.size());
        count.forEach((key, value) -> {
            ArticleCategoryCountVO countVO = new ArticleCategoryCountVO();
            ArticleCategory articleCategory = categoryMap.get(key);
            countVO.setId(key);
            countVO.setName(articleCategory.getName());
            countVO.setDescription(articleCategory.getDescription());
            countVO.setCount(value);
            cateCountVOList.add(countVO);
        });
        return cateCountVOList;
    }

    /**
     * 标签数量使用情况统计
     *
     * @return 统计列表
     * @author Ezhixuan
     */
    @Override
    public List<ArticleTagCountVO> getTagCount() {
        List<String> tagList =
            articleService.listObjs(Wrappers.<Article>lambdaQuery().select(Article::getTagIds), o -> (String)o);
        if (ObjectUtils.isEmpty(tagList))
            return new ArrayList<>();
        Map<Long, Integer> count = tagList.stream().flatMap(id -> Arrays.stream(id.split(","))).map(Long::parseLong)
            .collect(Collectors.toMap(id -> id, id -> 1, Integer::sum));

        Map<Long, String> tagMap = tagService.listByIds(count.keySet()).stream()
            .collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));
        List<ArticleTagCountVO> tagCountVOList = new ArrayList<>(tagMap.size());
        count.forEach((key, value) -> {
            ArticleTagCountVO countVO = new ArticleTagCountVO();
            countVO.setId(key);
            countVO.setName(tagMap.get(key));
            countVO.setCount(value);
            tagCountVOList.add(countVO);
        });
        return tagCountVOList;
    }


    /**
     * 获取文章详情,会进行缓存
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
        ArticlePageVO articlePageVO = toPageVO(articlePageDTO);
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
     * @author Ezhixuan
     * @param articleId 文章 id
     * @param viewCount 查看次数
     */
    @Async
    @Override
    public void asyncUpdateViewCount(long articleId, Integer viewCount) {
        articleService.update(Wrappers.<Article>lambdaUpdate().eq(Article::getId, articleId).set(Article::getViewCount, viewCount));
    }
}
