package com.ezhixuan.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.domain.WebSocketMessageDTO;
import com.ezhixuan.blog.service.impl.WebSocketServiceImpl;

import lombok.RequiredArgsConstructor;

/**
 * WebSocket 测试控制器
 * 用于测试 WebSocket 消息发送功能
 */
@RestController
@RequestMapping("/websocket/test")
@RequiredArgsConstructor
public class WebSocketTestController {

    private final WebSocketServiceImpl webSocketService;

    /**
     * 发送成功消息给所有用户
     */
    @PostMapping("/success")
    public String sendSuccessMessage(@RequestParam String title, @RequestParam String content) {
        webSocketService.sendSuccessMessage(title, content);
        return "成功消息已发送";
    }

    /**
     * 发送信息消息给所有用户
     */
    @PostMapping("/info")
    public String sendInfoMessage(@RequestParam String title, @RequestParam String content) {
        webSocketService.sendInfoMessage(title, content);
        return "信息消息已发送";
    }

    /**
     * 发送警告消息给所有用户
     */
    @PostMapping("/warning")
    public String sendWarningMessage(@RequestParam String title, @RequestParam String content) {
        webSocketService.sendWarningMessage(title, content);
        return "警告消息已发送";
    }

    /**
     * 发送错误消息给所有用户
     */
    @PostMapping("/error")
    public String sendErrorMessage(@RequestParam String title, @RequestParam String content) {
        webSocketService.sendErrorMessage(title, content);
        return "错误消息已发送";
    }

    /**
     * 发送消息给指定用户
     */
    @PostMapping("/user/{userId}")
    public String sendMessageToUser(@PathVariable Long userId,
                                    @RequestParam String type,
                                    @RequestParam String title,
                                    @RequestParam String content) {
        WebSocketMessageDTO messageDTO = WebSocketMessageDTO.builder()
                .type(type)
                .title(title)
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();

        webSocketService.sendStructuredMessageToUser(userId, messageDTO);
        return "消息已发送给用户 " + userId;
    }

    /**
     * 发送消息给多个用户
     */
    @PostMapping("/users")
    public String sendMessageToUsers(@RequestParam List<Long> userIds,
                                     @RequestParam String type,
                                     @RequestParam String title,
                                     @RequestParam String content) {
        WebSocketMessageDTO messageDTO = WebSocketMessageDTO.builder()
                .type(type)
                .title(title)
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();

        webSocketService.sendStructuredMessageToUsers(userIds, messageDTO);
        return "消息已发送给 " + userIds.size() + " 个用户";
    }

    /**
     * 发送带数据的消息
     */
    @PostMapping("/with-data")
    public String sendMessageWithData(@RequestParam String type,
                                      @RequestParam String title,
                                      @RequestParam String content,
                                      @RequestBody(required = false) Object data) {
        WebSocketMessageDTO messageDTO = WebSocketMessageDTO.withData(type, title, content, data);
        webSocketService.sendStructuredMessage(messageDTO);
        return "带数据的消息已发送";
    }

    /**
     * 获取在线用户数量
     */
    @GetMapping("/online-count")
    public Integer getOnlineCount() {
        return webSocketService.onlineCount();
    }

    /**
     * 检查管理员是否在线
     */
    @GetMapping("/admin-online")
    public Boolean isAdminOnline() {
        return webSocketService.adminOnline();
    }
}
