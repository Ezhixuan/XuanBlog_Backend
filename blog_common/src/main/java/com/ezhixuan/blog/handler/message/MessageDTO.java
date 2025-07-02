package com.ezhixuan.blog.handler.message;

import java.util.List;

import lombok.Data;

@Data
public class MessageDTO {

    /**
     * 消息
     */
    private String message;

    /**
     * 接收者
     */
    private List<Long> acceptId;
}
