package com.ezhixuan.xuanblog_backend.controller.Article;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.xuanblog_backend.service.ArticleThumbService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleThumbController {

    private final ArticleThumbService thumbService;

    @GetMapping("/thumb/{id}")
    @Operation(summary = "点赞/取消点赞")
    public BaseResponse<Boolean> doThumb(@PathVariable Long id) {
        return R.success(thumbService.doThumb(id));
    }
}
