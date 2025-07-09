package com.ezhixuan.blog.controller.article;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.ArticleTag;
import com.ezhixuan.blog.domain.vo.CountVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.ArticleTagService;
import com.ezhixuan.blog.service.LinkArticleTagService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class ArticleTagController {

    private final ArticleTagService tagService;
    private final ArticleOperateService operateService;
    private final LinkArticleTagService linkArticleTagService;

    @Cache(key = RedisKeyConstant.LIST_TAG_KEY)
    @Operation(summary = "获取标签列表")
    @GetMapping("/list")
    public BaseResponse<List<ArticleTag>> getTagList() {
        return R.success(tagService.list());
    }

    @Log
    @Cache(key = RedisKeyConstant.LIST_TAG_KEY, operateType = Cache.CacheOperateType.DELETE)
    @Operation(summary = "新增标签")
    @PostMapping
    public BaseResponse<ArticleTag> addTag(@RequestBody ArticleTag tag) {
        tagService.save(tag);
        return R.success(tag);
    }

    @Log
    @Cache(key = RedisKeyConstant.LIST_TAG_KEY, operateType = Cache.CacheOperateType.DELETE)
    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteTag(@PathVariable Long id) {
        return R.success(operateService.deleteTagById(id));
    }

    @Operation(summary = "标签统计计数")
    @GetMapping("/count")
    public BaseResponse<List<CountVO>> getTagCount() {
        return R.success(linkArticleTagService.queryTagCount());
    }
}
