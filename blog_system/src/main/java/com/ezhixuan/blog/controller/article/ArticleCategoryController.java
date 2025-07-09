package com.ezhixuan.blog.controller.article;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.domain.vo.CountVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ArticleCategoryService;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.LinkArticleCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;
    private final ArticleOperateService operateService;
    private final LinkArticleCategoryService linkArticleCategoryService;

    @Cache(key = RedisKeyConstant.LIST_CATEGORY_KEY)
    @Operation(summary = "获取分类列表")
    @GetMapping("/list")
    public BaseResponse<List<ArticleCategory>> getCategoryList() {
        return R.success(categoryService.list());
    }

    @Log
    @Cache(key = RedisKeyConstant.LIST_CATEGORY_KEY, operateType = Cache.CacheOperateType.DELETE)
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteCategory(@PathVariable Long id) {
        return R.success(operateService.deleteCategoryById(id));
    }

    @Log
    @Cache(key = RedisKeyConstant.LIST_CATEGORY_KEY, operateType = Cache.CacheOperateType.DELETE)
    @Operation(summary = "创建分类")
    @PostMapping
    public BaseResponse<Void> addCategory(@RequestBody ArticleCategory category) {
        categoryService.save(category);
        return R.success();
    }

    @Operation(summary = "分类统计计数")
    @GetMapping("/count")
    public BaseResponse<List<CountVO>> queryCategoryCount() {
        return R.success(linkArticleCategoryService.queryCategoryCount());
    }
}
