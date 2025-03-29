package com.ezhixuan.xuanblog_backend.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleContent;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.ezhixuan.xuanblog_backend.domain.vo.*;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.*;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    final ArticleQueryService queryService;
    final ArticleEditService editService;
    final ArticleTagService tagService;
    final ArticleCategoryService categoryService;
    final ArticleContentService contentService;

    @PostMapping("/list")
    public BaseResponse<PageResponse<ArticlePageVO>> getArticlePageList(@RequestBody ArticleQueryDTO articleQueryDTO) {
        IPage<ArticlePageVO> articlePageVOList = queryService.getArticlePageVOList(articleQueryDTO);
        return R.list(articlePageVOList);
    }

    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<String> doSubmitArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
        editService.doSubmitArticle(articleSubmitDTO);
        return R.success();
    }

    @GetMapping("/tags")
    @Cache(key = "tags")
    public BaseResponse<List<ArticleTagVO>> getArticleTagList() {
        List<ArticleTagVO> tagVOList =
            tagService.list().stream().map(item -> BeanUtil.copyProperties(item, ArticleTagVO.class)).toList();
        return R.success(tagVOList);
    }

    @GetMapping("/categories")
    @Cache(key = "categories")
    public BaseResponse<List<ArticleCategoryVO>> getArticleCategoryList() {
        List<ArticleCategoryVO> categoryVOList = categoryService.list().stream()
            .map(item -> BeanUtil.copyProperties(item, ArticleCategoryVO.class)).toList();
        return R.success(categoryVOList);
    }

    @PostMapping("/blogs")
    public BaseResponse<ArticleInfoVO> getArticleInfo(String id) {
        ThrowUtils.throwIf(Objects.isNull(id), ErrorCode.PARAMS_ERROR);
        // 构建查询条件
        ArticleQueryDTO articleQueryDTO = new ArticleQueryDTO();
        articleQueryDTO.setIds(Collections.singleton(Long.parseLong(id)));
        IPage<ArticlePageVO> articlePageVOList = queryService.getArticlePageVOList(articleQueryDTO);
        ArticleInfoVO articleInfoVO = new ArticleInfoVO();
        articlePageVOList.getRecords().stream().findAny()
            .ifPresent(item -> BeanUtil.copyProperties(item, articleInfoVO));

        // 补充文章内容
        ArticleContent content = contentService.getById(id);
        if (Objects.nonNull(content)) {
            articleInfoVO.setContent(content.getContent());
        }
        return R.success(articleInfoVO);
    }

    @PostMapping("/tag/add")
    public BaseResponse<ArticleTagVO> submitTag(@RequestBody String tagName) {
        ArticleTag articleTag = new ArticleTag();
        ArticleTag tag = this.tagService.getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getName, tagName));
        if (Objects.isNull(tag)) {
            articleTag.setName(tagName);
            this.tagService.save(articleTag);
        }
        ArticleTagVO articleTagVO = BeanUtil.copyProperties(articleTag, ArticleTagVO.class);
        return R.success(articleTagVO);
    }

    @PostMapping("/category/add")
    public BaseResponse<ArticleCategoryVO> submitCategory(@RequestBody String categoryName) {
        ArticleCategory articleCategory = new ArticleCategory();
        ArticleCategory category = this.categoryService.getOne(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getName, categoryName));
        if (Objects.isNull(category)) {
            articleCategory.setName(categoryName);
            this.categoryService.save(articleCategory);
        }
        ArticleCategoryVO articleCategoryVO = BeanUtil.copyProperties(articleCategory, ArticleCategoryVO.class);
        return R.success(articleCategoryVO);
    }

    @GetMapping("/tags/count")
    public BaseResponse<List<ArticleTagCountVO>> getTagCount() {
        return R.success(queryService.getTagCount());
    }

    @GetMapping("/categories/count")
    public BaseResponse<List<ArticleCategoryCountVO>> getCategoryCount() {
        return R.success(queryService.getCategoryCount());
    }

}
