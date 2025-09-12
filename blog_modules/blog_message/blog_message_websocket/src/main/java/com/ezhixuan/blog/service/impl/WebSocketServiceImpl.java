package com.ezhixuan.blog.service.impl;

import com.ezhixuan.blog.controller.MessageWebSocketServer;
import com.ezhixuan.blog.domain.WebSocketMessageDTO;
import com.ezhixuan.blog.handler.message.MessageDTO;
import com.ezhixuan.blog.handler.message.MessageHandler;
import com.ezhixuan.blog.handler.message.MessageModel;
import com.ezhixuan.blog.handler.message.MessageModelConstant;
import com.ezhixuan.blog.service.UserService;
import com.ezhixuan.blog.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements MessageHandler, WebSocketService {

    private final MessageWebSocketServer webSocketServer;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public MessageModel getMessageModel() {
        return MessageModelConstant.WEBSOCKET;
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        if (Objects.isNull(messageDTO) || !StringUtils.hasText(messageDTO.getMessage())) {
            return;
        }
        if (CollectionUtils.isEmpty(messageDTO.getAcceptId())) {
            webSocketServer.sendAllMessage(messageDTO.getMessage());
            return;
        }
        webSocketServer.sendMoreMessage(messageDTO.getAcceptId(), messageDTO.getMessage());
    }

    @Override
    public int onlineCount() {
        return webSocketServer.getOnlineUserList().size();
    }

    @Override
    public boolean adminOnline() {
        AtomicBoolean online = new AtomicBoolean(false);
        webSocketServer.getOnlineUserList().stream().filter(userService::isAdmin).findFirst().ifPresent(userId -> {
            online.set(true);
        });
        return online.get();
    }

    /**
     * 发送结构化消息给所有用户
     *
     * @param messageDTO 消息DTO
     */
    public void sendStructuredMessage(WebSocketMessageDTO messageDTO) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);
            webSocketServer.sendAllMessage(jsonMessage);
            log.info("已向所有用户发送结构化消息: {}", messageDTO.getTitle());
        } catch (JsonProcessingException e) {
            log.error("序列化消息失败", e);
        }
    }

    /**
     * 发送结构化消息给指定用户
     *
     * @param userId 用户ID
     * @param messageDTO 消息DTO
     */
    public void sendStructuredMessageToUser(Long userId, WebSocketMessageDTO messageDTO) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);
            webSocketServer.sendOneMessage(userId, jsonMessage);
            log.info("已向用户{}发送结构化消息: {}", userId, messageDTO.getTitle());
        } catch (JsonProcessingException e) {
            log.error("序列化消息失败", e);
        }
    }

    /**
     * 发送结构化消息给多个用户
     *
     * @param userIds 用户ID列表
     * @param messageDTO 消息DTO
     */
    public void sendStructuredMessageToUsers(java.util.List<Long> userIds, WebSocketMessageDTO messageDTO) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);
            webSocketServer.sendMoreMessage(userIds, jsonMessage);
            log.info("已向{}个用户发送结构化消息: {}", userIds.size(), messageDTO.getTitle());
        } catch (JsonProcessingException e) {
            log.error("序列化消息失败", e);
        }
    }

    /**
     * 发送成功消息
     *
     * @param title 消息标题
     * @param content 消息内容
     */
    public void sendSuccessMessage(String title, String content) {
        sendStructuredMessage(WebSocketMessageDTO.success(title, content));
    }

    /**
     * 发送信息消息
     *
     * @param title 消息标题
     * @param content 消息内容
     */
    public void sendInfoMessage(String title, String content) {
        sendStructuredMessage(WebSocketMessageDTO.info(title, content));
    }

    /**
     * 发送警告消息
     *
     * @param title 消息标题
     * @param content 消息内容
     */
    public void sendWarningMessage(String title, String content) {
        sendStructuredMessage(WebSocketMessageDTO.warning(title, content));
    }

    /**
     * 发送错误消息
     *
     * @param title 消息标题
     * @param content 消息内容
     */
    public void sendErrorMessage(String title, String content) {
        sendStructuredMessage(WebSocketMessageDTO.error(title, content));
    }
}
