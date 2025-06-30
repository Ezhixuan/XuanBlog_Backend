package com.ezhixuan.blog.handler;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ezhixuan.blog.domain.enums.MessageTypeEnum;
import com.ezhixuan.blog.handler.message.MessageManager;

import lombok.RequiredArgsConstructor;

/**
 * 消息工厂
 */
@Component
@RequiredArgsConstructor
public class MessageFactory {

    private final List<MessageManager> serviceList;

    public MessageManager getInstance(MessageTypeEnum typeEnum) {
        return serviceList.stream().filter(service -> Objects.equals(service.getMessageTypeEnum(), typeEnum)).findAny()
            .orElseThrow(() -> new RuntimeException("未找到对应的消息服务"));
    }
}
