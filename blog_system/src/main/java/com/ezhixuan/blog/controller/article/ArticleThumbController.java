package com.ezhixuan.blog.controller.article;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.entity.OperationById;
import com.ezhixuan.blog.common.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.service.ArticleThumbService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleThumbController {

    private final ArticleThumbService thumbService;

    @PostMapping("/thumb")
    @Operation(summary = "点赞/取消点赞")
    public BaseResponse<Boolean> doThumb(@RequestBody OperationById request) {
        return R.success(thumbService.doThumb(request.getId()));
    }
}
