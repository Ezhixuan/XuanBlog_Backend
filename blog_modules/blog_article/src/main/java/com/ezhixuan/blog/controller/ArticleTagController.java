package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.entity.ArticleTag;
import com.ezhixuan.blog.controller.vo.CountVO;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.ArticleTagService;
import com.ezhixuan.blog.service.LinkArticleTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@Tag(name = "tagController", description = "提供标签相关的操作")
public class ArticleTagController {

  private final ArticleTagService tagService;
  private final ArticleOperateService operateService;
  private final LinkArticleTagService linkArticleTagService;

  @Cache(key = RedisKeyConstant.LIST_TAG_KEY)
  @GetMapping
  @Operation(summary = "获取标签列表")
  public BaseResponse<List<ArticleTag>> getTagList() {
    return R.success(tagService.list());
  }

  @Log
  @Cache(key = RedisKeyConstant.LIST_TAG_KEY, operateType = Cache.CacheOperateType.DELETE)
  @PostMapping
  @Operation(summary = "新增标签")
  public BaseResponse<ArticleTag> addTag(@RequestBody ArticleTag tag) {
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
    Map<Long, String> idNameMap =
        tagService.list().stream()
            .collect(Collectors.toMap(ArticleTag::getId, ArticleTag::getName));
    List<CountVO> countVOS = linkArticleTagService.queryTagCount();
    for (CountVO countVO : countVOS) {
      countVO.setName(idNameMap.get(countVO.getId()));
    }
    return R.list(countVOS);
  }
}
