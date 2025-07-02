package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Operation(summary = "获取当前在线人数")
    @GetMapping("/online")
    public BaseResponse<Integer> getOnlineNum() {
        return R.success(webSocketService.onlineCount());
    }

    @Operation(summary = "判断管理员是否在线")
    @GetMapping("/admin/online")
    public BaseResponse<Boolean> isAdminOnline() {
        return R.success(webSocketService.adminOnline());
    }
}
