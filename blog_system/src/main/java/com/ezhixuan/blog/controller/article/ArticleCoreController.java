package com.ezhixuan.blog.controller.article;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.domain.vo.ArticleInfoVO;
import com.ezhixuan.blog.domain.vo.ArticlePageVO;
import com.ezhixuan.blog.service.ArticleEditService;
import com.ezhixuan.blog.service.ArticleQueryService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleCoreController {

    private final ArticleQueryService queryService;
    private final ArticleEditService editService;

    @PostMapping("/list")
    @Operation(summary = "获取分页文章列表")
    public BaseResponse<PageResponse<ArticlePageVO>> getArticlePageList(@RequestBody ArticleQueryDTO articleQueryDTO) {
        IPage<ArticlePageVO> articlePageVOList = queryService.getArticlePageVOList(articleQueryDTO);
        return R.list(articlePageVOList);
    }

    @Log
    @PostMapping("/submit")
    @Operation(summary = "提交博客")
    @SaCheckLogin
    public BaseResponse<String> doSubmitArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
        editService.doSubmitArticle(articleSubmitDTO);
        return R.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public BaseResponse<ArticleInfoVO> getArticleInfo(@PathVariable Long id) {
        return R.success(queryService.getArticleInfoVO(id));
    }
}
