package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.common.PageRequest;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticlePageDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;
import com.ezhixuan.xuanblog_backend.service.ArticleCategoryService;
import com.ezhixuan.xuanblog_backend.service.ArticleQueryService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;
import com.ezhixuan.xuanblog_backend.service.ArticleTagService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    final ArticleService articleService;
    final ArticleCategoryService categoryService;
    final ArticleTagService tagService;

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
        List<Long> tagIds =
            Arrays.stream(articlePageDTO.getTagIds().split(",")).toList().stream().map(Long::parseLong).toList();
        Map<Long, String> tag =
            tagService.listByIds(tagIds).stream().collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));
        articlePageVO.setTagMap(tag);

        ArticleCategory categoryById = categoryService.getById(articlePageDTO.getCategoryId());
        articlePageVO.setCategoryId(articlePageDTO.getCategoryId());
        articlePageVO.setCategoryName(categoryById.getName());
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
}
