package com.ezhixuan.blog.handler.message;

public class MessageModelConstant {

    public static final MessageModel WEBSOCKET = MessageModel.builder()
            .messageType("WEBSOCKET")
            .desc("websocket")
            .build();

    public static final MessageModel EMAIL = MessageModel.builder()
            .messageType("EMAIL")
            .desc("email")
            .build();
}
