package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.vo.CountVO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.ArticleQueryService;
import com.ezhixuan.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@Tag(name = "tagController", description = "提供标签相关的操作")
public class ArticleTagController {

  private final TagService tagService;
  private final ArticleOperateService operateService;
  private final ArticleQueryService queryService;

  @Cache(key = RedisKeyConstant.LIST_TAG_KEY)
  @GetMapping
  @Operation(summary = "获取标签列表")
  public PageResponse<com.ezhixuan.blog.entity.Tag> getTagList() {
    return R.list(tagService.list());
  }

  @Log
  @Cache(key = RedisKeyConstant.LIST_TAG_KEY, operateType = Cache.CacheOperateType.DELETE)
  @PostMapping
  @Operation(summary = "新增标签")
  public BaseResponse<com.ezhixuan.blog.entity.Tag> addTag(@RequestBody com.ezhixuan.blog.entity.Tag tag) {
    tagService.save(tag);
    return R.success(tag);
  }

  @Log
  @Cache(key = RedisKeyConstant.LIST_TAG_KEY, operateType = Cache.CacheOperateType.DELETE)
  @DeleteMapping("/{id}")
  @Operation(summary = "删除标签")
  public BaseResponse<Boolean> deleteTag(@PathVariable Long id) {
    return R.success(operateService.deleteTagById(id));
  }

  @GetMapping("/count")
  @Operation(summary = "标签统计计数")
  public PageResponse<CountVO> getTagCount() {
    return R.list(queryService.getTagCountVo(10));
  }
}
