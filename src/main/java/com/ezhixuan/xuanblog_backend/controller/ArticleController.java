package com.ezhixuan.xuanblog_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;
import com.ezhixuan.xuanblog_backend.service.ArticleEditService;
import com.ezhixuan.xuanblog_backend.service.ArticleQueryService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    final ArticleQueryService queryService;
    final ArticleEditService editService;

    @PostMapping("/list")
    public BaseResponse<PageResponse<ArticlePageVO>> getArticlePageList(@RequestBody ArticleQueryDTO articleQueryDTO) {
        IPage<ArticlePageVO> articlePageVOList = queryService.getArticlePageVOList(articleQueryDTO);
        return R.list(articlePageVOList);
    }

    @PostMapping("/add")
    @SaCheckLogin
    public BaseResponse<String> doSubmitArticle(@RequestBody ArticleSubmitDTO articleSubmitDTO) {
        editService.doSubmitArticle(articleSubmitDTO);
        return R.success();
    }

}
