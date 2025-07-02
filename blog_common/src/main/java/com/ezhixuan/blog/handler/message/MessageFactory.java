package com.ezhixuan.blog.handler.message;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理器
 *
 * @author ezhixuan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageFactory {

    private final List<MessageHandler> serviceList;
    private static List<MessageModel> availableTypes;

    @PostConstruct
    public void init() {
        log.info("===========消息服务初始化===================");
        if (!isEmpty(serviceList)) {
            initAvailableTypes();
            log.info("可用消息服务：{}", availableTypes);
            log.info("===========消息服务初始化完成===================");
        } else {
            log.error("消息服务未初始化");
        }
    }

    /**
     * 获取默认消息管理器实例
     *
     * @return MessageManager
     */
    public MessageHandler getInstance(MessageModel model) {
        if (isNull(model) || !StringUtils.hasText(model.getMessageType())) {
            throw new RuntimeException("请确定选择的消息服务");
        }
        return serviceList.stream()
            .filter(service -> Objects.equals(service.getMessageModel().getMessageType(), model.getMessageType()))
            .findAny()
            .orElseThrow(() -> new RuntimeException("未提供的消息服务" + model.getDesc()));
    }


    /**
     * 获取所有可用的消息类型
     *
     * @return 可用的消息类型列表
     */
    public List<MessageModel> getAvailableTypes() {
        if (isEmpty(serviceList)) {
            return Collections.emptyList();
        } else if (isEmpty(availableTypes)) {
            initAvailableTypes();
        }
        return new ArrayList<>(availableTypes);
    }

    /**
     * 初始化可用类型列表
     */
    private void initAvailableTypes() {
        if (isEmpty(serviceList)) {
            return;
        }
        availableTypes = serviceList.stream()
            .map(MessageHandler::getMessageModel)
            .filter(Objects::nonNull)
            .toList();
    }
}
