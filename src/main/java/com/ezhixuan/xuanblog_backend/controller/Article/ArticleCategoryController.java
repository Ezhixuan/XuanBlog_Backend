package com.ezhixuan.xuanblog_backend.controller.Article;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.annotation.Log;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleCategoryCountVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleCategoryVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleCategoryService;
import com.ezhixuan.xuanblog_backend.service.ArticleQueryService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;
    private final ArticleService articleService;
    private final ArticleQueryService queryService;

    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    @Cache(key = "categories")
    public BaseResponse<List<ArticleCategoryVO>> getArticleCategoryList() {
        List<ArticleCategoryVO> categoryVOList = categoryService.list().stream()
            .map(item -> BeanUtil.copyProperties(item, ArticleCategoryVO.class)).toList();
        return R.success(categoryVOList);
    }

    @Log
    @Operation(summary = "删除分类")
    @DeleteMapping("/category/{id}")
    public BaseResponse<String> deleteCategory(@PathVariable Long id) {
        boolean exists = articleService.lambdaQuery().apply("FIND_IN_SET({0}, category_ids)", id).exists();
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该分类已存在绑定文章,请先前往文章解绑");
        categoryService.removeById(id);
        return R.success();
    }

    @Log
    @Operation(summary = "创建分类")
    @PostMapping("/category/add")
    public BaseResponse<ArticleCategoryVO> submitCategory(@RequestParam String name) {
        ArticleCategory articleCategory = new ArticleCategory();
        ArticleCategory category =
            this.categoryService.getOne(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getName, name));
        if (Objects.isNull(category)) {
            articleCategory.setName(name);
            this.categoryService.save(articleCategory);
        }
        ArticleCategoryVO articleCategoryVO = BeanUtil.copyProperties(articleCategory, ArticleCategoryVO.class);
        return R.success(articleCategoryVO);
    }

    @GetMapping("/categories/count")
    @Operation(summary = "分类统计计数")
    public BaseResponse<List<ArticleCategoryCountVO>> getCategoryCount() {
        return R.success(queryService.getCategoryCount());
    }
}
