package com.ezhixuan.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ezhixuan.blog.domain.enums.MessageTypeEnum;

import lombok.Data;

/**
 * 消息传输 DTO
 */
@Data
public class MessageDTO {

    /**
     * 消息唯一 id
     */
    private String messageId;

    /**
     * 消息类型
     */
    private MessageTypeEnum messageType;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 发送者 id
     */
    private Long senderId;

    /**
     * 接受者列表
     */
    private List<Long> recipients;

    /**
     * 消息主题
     */
    private String subject;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 拓展参数
     */
    private Map<String, Object> extra;

    /**
     * 优先级 (1 ~ 10)
     */
    private Integer priority;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 需要持久化
     */
    private Boolean needPersist;

}
