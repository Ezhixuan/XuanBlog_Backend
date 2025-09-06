package com.ezhixuan.blog.domain;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * WebSocket 消息 DTO 用于发送结构化消息给前端
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {

    /**
     * 消息类型：success, info, warning, error
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息数据（可选）
     */
    private Object data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 创建成功消息
     */
    public static WebSocketMessageDTO success(String title, String content) {
        return WebSocketMessageDTO.builder().type("success").title(title).content(content)
            .timestamp(System.currentTimeMillis()).build();
    }

    /**
     * 创建信息消息
     */
    public static WebSocketMessageDTO info(String title, String content) {
        return WebSocketMessageDTO.builder().type("info").title(title).content(content)
            .timestamp(System.currentTimeMillis()).build();
    }

    /**
     * 创建警告消息
     */
    public static WebSocketMessageDTO warning(String title, String content) {
        return WebSocketMessageDTO.builder().type("warning").title(title).content(content)
            .timestamp(System.currentTimeMillis()).build();
    }

    /**
     * 创建错误消息
     */
    public static WebSocketMessageDTO error(String title, String content) {
        return WebSocketMessageDTO.builder().type("error").title(title).content(content)
            .timestamp(System.currentTimeMillis()).build();
    }

    /**
     * 创建带数据的消息
     */
    public static WebSocketMessageDTO withData(String type, String title, String content, Object data) {
        return WebSocketMessageDTO.builder().type(type).title(title).content(content).data(data)
            .timestamp(System.currentTimeMillis()).build();
    }
}
