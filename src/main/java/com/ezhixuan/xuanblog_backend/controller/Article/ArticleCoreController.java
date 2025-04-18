package com.ezhixuan.xuanblog_backend.controller.Article;

import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.annotation.Log;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleInfoVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleEditService;
import com.ezhixuan.xuanblog_backend.service.ArticleQueryService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleCoreController {

    private final ArticleQueryService queryService;
    private final ArticleEditService editService;
    private final ArticleService articleService;

    @PostMapping("/list")
    @Operation(summary = "获取分页文章列表")
    public BaseResponse<PageResponse<ArticlePageVO>> getArticlePageList(@RequestBody ArticleQueryDTO articleQueryDTO) {
        IPage<ArticlePageVO> articlePageVOList = queryService.getArticlePageVOList(articleQueryDTO);
        return R.list(articlePageVOList);
    }

    @Log
    @PostMapping("/add")
    @Operation(summary = "提交博客")
    @SaCheckLogin
    public BaseResponse<String> doSubmitArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
        editService.doSubmitArticle(articleSubmitDTO);
        return R.success();
    }

    @PostMapping("/blogs")
    @Operation(summary = "获取文章详情")
    public BaseResponse<ArticleInfoVO> getArticleInfo(String id) {
        ThrowUtils.throwIf(Objects.isNull(id), ErrorCode.PARAMS_ERROR);
        // 构建查询条件
        long articleId = Long.parseLong(id);
        Article article = new Article();
        /*
        内部查询调用了缓存，所以这里额外进行单字段查询操作
         */
        Integer viewCount = articleService.getObj(
            Wrappers.<Article>lambdaQuery().select(Article::getViewCount).eq(Article::getId, articleId),
            o -> (Integer)o) + 1;
        article.setId(articleId);
        article.setViewCount(viewCount);
        articleService.updateById(article);
        return R.success(queryService.getArticleInfo(articleId));
    }
}
