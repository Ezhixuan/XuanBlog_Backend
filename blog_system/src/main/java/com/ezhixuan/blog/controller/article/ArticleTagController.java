package com.ezhixuan.blog.controller.article;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.entity.article.ArticleTag;
import com.ezhixuan.blog.domain.vo.ArticleTagVO;
import com.ezhixuan.blog.domain.vo.CountVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.ArticleQueryService;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.ArticleTagService;
import com.ezhixuan.blog.service.LinkArticleTagService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class ArticleTagController {

    private final ArticleTagService tagService;
    private final ArticleService articleService;
    private final ArticleQueryService queryService;
    private final LinkArticleTagService linkArticleTagService;

    @GetMapping("/list")
    @Cache(key = "list:tags")
    @Operation(summary = "获取标签列表")
    public BaseResponse<List<ArticleTagVO>> getTagList() {
        List<ArticleTagVO> tagVOList =
            tagService.list().stream().map(item -> BeanUtil.copyProperties(item, ArticleTagVO.class)).toList();
        return R.success(tagVOList);
    }

    @Log
    @PutMapping("/{name}")
    @Operation(summary = "新增标签")
    public BaseResponse<ArticleTagVO> add(@PathVariable String name) {
        ArticleTag articleTag = new ArticleTag();
        ArticleTag tag = this.tagService.getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getName, name));
        if (Objects.isNull(tag)) {
            articleTag.setName(name);
            this.tagService.save(articleTag);
        }
        ArticleTagVO articleTagVO = BeanUtil.copyProperties(articleTag, ArticleTagVO.class);
        return R.success(articleTagVO);
    }

    @GetMapping("/count")
    @Operation(summary = "标签统计计数")
    public BaseResponse<List<CountVO>> getTagCount() {
        return R.success(linkArticleTagService.getTagCount());
    }

    @Log
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public BaseResponse<String> deleteTag(@PathVariable Long id) {
        boolean exists = articleService.lambdaQuery().apply("FIND_IN_SET({0}, tag_ids)", id).exists();
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该标签已存在绑定文章,请先前往文章解绑");
        tagService.removeById(id);
        return R.success();
    }
}
