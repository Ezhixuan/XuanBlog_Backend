package com.ezhixuan.blog.controller.article;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.blog.domain.vo.ArticleInfoVO;
import com.ezhixuan.blog.domain.vo.ArticlePageVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ArticleOperateService;
import com.ezhixuan.blog.service.ArticleQueryService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleCoreController {

    private final ArticleQueryService queryService;
    private final ArticleOperateService operateService;

    @PostMapping("/list")
    @Operation(summary = "获取分页文章列表")
    public BaseResponse<PageResponse<ArticlePageVO>> getArticleListPage(@RequestBody ArticleQueryDTO articleQueryDTO) {
        return R.list(queryService.pageListByDTO(articleQueryDTO));
    }

    @Log
    @SaCheckLogin
    @PostMapping
    @Operation(summary = "提交博客")
    public BaseResponse<String> addOrUpdateArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
        operateService.doSubmitArticle(articleSubmitDTO);
        return R.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public BaseResponse<ArticleInfoVO> getArticleInfo(@PathVariable Long id) {
        return R.success(queryService.getArticleInfoVO(id));
    }

    @Log
    @DeleteMapping("/{id}")
    @Operation(description = "删除文章")
    public BaseResponse<Boolean> deleteArticle(@PathVariable Long id) {
        return R.success(operateService.deleteArticleById(id));
    }
}
