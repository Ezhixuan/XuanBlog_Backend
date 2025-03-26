package com.ezhixuan.xuanblog_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;

@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return R.success("ok");
    }
}
