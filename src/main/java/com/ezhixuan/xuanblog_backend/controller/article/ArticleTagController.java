package com.ezhixuan.xuanblog_backend.controller.article;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.annotation.Log;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleTagCountVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleTagVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleQueryService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;
import com.ezhixuan.xuanblog_backend.service.ArticleTagService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleTagController {

    private final ArticleTagService tagService;
    private final ArticleService articleService;
    private final ArticleQueryService queryService;

    @GetMapping("/tags")
    @Operation(summary = "获取标签列表")
    @Cache(key = "tags")
    public BaseResponse<List<ArticleTagVO>> getArticleTagList() {
        List<ArticleTagVO> tagVOList =
            tagService.list().stream().map(item -> BeanUtil.copyProperties(item, ArticleTagVO.class)).toList();
        return R.success(tagVOList);
    }

    @Log
    @PostMapping("/tag/add")
    @Operation(summary = "新增标签")
    public BaseResponse<ArticleTagVO> submitTag(@RequestParam String name) {
        ArticleTag articleTag = new ArticleTag();
        ArticleTag tag = this.tagService.getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getName, name));
        if (Objects.isNull(tag)) {
            articleTag.setName(name);
            this.tagService.save(articleTag);
        }
        ArticleTagVO articleTagVO = BeanUtil.copyProperties(articleTag, ArticleTagVO.class);
        return R.success(articleTagVO);
    }

    @GetMapping("/tags/count")
    @Operation(summary = "标签统计计数")
    public BaseResponse<List<ArticleTagCountVO>> getTagCount() {
        return R.success(queryService.getTagCount());
    }

    @Log
    @DeleteMapping("/tag/{id}")
    @Operation(summary = "删除标签")
    public BaseResponse<String> deleteTag(@PathVariable Long id) {
        boolean exists = articleService.lambdaQuery().apply("FIND_IN_SET({0}, tag_ids)", id).exists();
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该标签已存在绑定文章,请先前往文章解绑");
        tagService.removeById(id);
        return R.success();
    }
}
