package com.ezhixuan.blog.controller.article;

import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.ArticleCategory;
import com.ezhixuan.blog.domain.vo.CountVO;
import com.ezhixuan.blog.service.ArticleCategoryService;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.LinkArticleCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "categoryController", description = "提供文章分类相关的操作")
public class ArticleCategoryController {

  private final ArticleCategoryService categoryService;
  private final ArticleOperateService operateService;
  private final LinkArticleCategoryService linkArticleCategoryService;

  @Cache(key = RedisKeyConstant.LIST_CATEGORY_KEY)
  @Operation(summary = "获取分类列表")
  @GetMapping("/list")
  public PageResponse<ArticleCategory> getCategoryList() {
    return R.list(categoryService.list());
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
  public PageResponse<CountVO> getCategoryCount() {
    Map<Long, String> idNameMap =
        categoryService.list().stream()
            .collect(Collectors.toMap(ArticleCategory::getId, ArticleCategory::getName));
    List<CountVO> countVOS = linkArticleCategoryService.queryCategoryCount();
    for (CountVO countVO : countVOS) {
      countVO.setName(idNameMap.get(countVO.getId()));
    }
    return R.list(countVOS);
  }
}
