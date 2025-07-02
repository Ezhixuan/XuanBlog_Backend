package com.ezhixuan.blog.service.impl;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ezhixuan.blog.controller.MessageWebSocketServer;
import com.ezhixuan.blog.handler.message.MessageDTO;
import com.ezhixuan.blog.handler.message.MessageHandler;
import com.ezhixuan.blog.handler.message.MessageModel;
import com.ezhixuan.blog.service.SysUserService;
import com.ezhixuan.blog.service.WebSocketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements MessageHandler, WebSocketService {

    private static final MessageModel MODEL = MessageModel.builder().messageType("websocket").desc("websocket消息").build();
    private final MessageWebSocketServer webSocketServer;
    private final SysUserService userService;

    @Override
    public MessageModel getMessageModel() {
        return MODEL;
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
}
