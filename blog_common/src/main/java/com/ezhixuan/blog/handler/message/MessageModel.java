package com.ezhixuan.blog.handler.message;

import lombok.Builder;
import lombok.Data;

/**
 * 消息模型
 *
 * @author ezhixuan
 */
@Data
@Builder
public class MessageModel {

    /**
     * 消息类型标识
     */
    private String messageType;

    /**
     * 消息类型描述
     */
    private String desc;
}
