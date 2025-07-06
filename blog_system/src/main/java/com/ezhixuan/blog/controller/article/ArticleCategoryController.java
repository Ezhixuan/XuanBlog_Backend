package com.ezhixuan.blog.controller.article;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.domain.vo.ArticleCategoryCountVO;
import com.ezhixuan.blog.domain.vo.ArticleCategoryVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.ArticleCategoryService;
import com.ezhixuan.blog.service.ArticleQueryService;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.LinkArticleCategoryService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;
    private final ArticleService articleService;
    private final ArticleQueryService queryService;
    private final LinkArticleCategoryService linkArticleCategoryService;

    @Operation(summary = "获取分类列表")
    @GetMapping("/list")
    @Cache(key = "list:category")
    public BaseResponse<List<ArticleCategoryVO>> getCategoryList() {
        List<ArticleCategoryVO> categoryVOList = categoryService.list().stream()
            .map(item -> BeanUtil.copyProperties(item, ArticleCategoryVO.class)).toList();
        return R.success(categoryVOList);
    }

    @Log
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteCategory(@PathVariable Long id) {
        boolean exists = articleService.lambdaQuery().apply("FIND_IN_SET({0}, category_ids)", id).exists();
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该分类已存在绑定文章,请先前往文章解绑");
        categoryService.removeById(id);
        return R.success();
    }

    @Log
    @Operation(summary = "创建分类")
    @PutMapping("/{name}")
    public BaseResponse<ArticleCategoryVO> add(@PathVariable String name) {
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

    @GetMapping("/count")
    @Operation(summary = "分类统计计数")
    public BaseResponse<List<ArticleCategoryCountVO>> getCategoryCount() {
        return R.success(linkArticleCategoryService.getCategoryCount());
    }
}
