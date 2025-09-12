package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "MainController", description = "系统入口")
public class MainController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return R.success("ok");
    }
}
