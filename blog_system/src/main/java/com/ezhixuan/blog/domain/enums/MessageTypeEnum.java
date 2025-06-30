package com.ezhixuan.blog.domain.enums;

import lombok.Getter;

/**
 * 消息类型枚举
 */
@Getter
public enum MessageTypeEnum {

    WEBSOCKET("websocket", "websocket 消息"),
    EMAIL("email", "邮件消息"),
    //...
    ;

    private final String type;
    private final String desc;

   MessageTypeEnum(String type, String desc) {
       this.type = type;
       this.desc = desc;
   }
}
