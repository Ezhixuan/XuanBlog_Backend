package com.ezhixuan.blog.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.controller.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.controller.vo.ArticleInfoVO;
import com.ezhixuan.blog.controller.vo.ArticlePageVO;
import com.ezhixuan.blog.service.ArticleThumbService;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.ArticleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Tag(name = "articleController", description = "提供对文章相关的操作")
public class ArticleCoreController {

  private final ArticleQueryService queryService;
  private final ArticleThumbService thumbService;
  private final ArticleOperateService operateService;

  @GetMapping
  @Operation(summary = "获取分页文章列表")
  public PageResponse<ArticlePageVO> getArticleListPage(ArticleQueryDTO articleQueryDTO) {
    return R.list(queryService.pageArticleListByDTO(articleQueryDTO));
  }

  @Log
  @SaCheckLogin
  @PostMapping
  @Operation(summary = "提交博客")
  public BaseResponse<Long> addOrUpdateArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
    return R.success(operateService.submitArticle(articleSubmitDTO));
  }

  @GetMapping("/{id}")
  @Operation(summary = "获取文章详情")
  public BaseResponse<ArticleInfoVO> getArticleInfo(@PathVariable Long id) {
    return R.success(queryService.getArticleVO(id));
  }

  @Log
  @Cache(
      key = RedisKeyConstant.ARTICLE_INFO_PRE_KEY + "#id",
      operateType = Cache.CacheOperateType.DELETE)
  @DeleteMapping("/{id}")
  @Operation(description = "删除文章")
  public BaseResponse<Boolean> deleteArticle(@PathVariable Long id) {
    return R.success(operateService.deleteArticleById(id));
  }

  @PutMapping("/thumb/{id}")
  @Operation(summary = "点赞/取消点赞")
  public BaseResponse<Boolean> doThumb(@PathVariable("id") Long id) {
    return R.success(thumbService.doThumb(id));
  }

  @GetMapping("/recommend")
  @Operation(summary = "获取推荐文章列表")
  public PageResponse<ArticlePageVO> getRecommendedList() {
      return R.list(queryService.getRecommendedList(5));
  }

}
