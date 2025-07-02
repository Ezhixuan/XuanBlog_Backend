package com.ezhixuan.blog.handler.message;

public interface MessageHandler {

    MessageModel getMessageModel();

    void sendMessage(MessageDTO messageDTO);
}
