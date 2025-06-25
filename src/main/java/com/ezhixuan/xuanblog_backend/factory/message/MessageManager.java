package com.ezhixuan.xuanblog_backend.factory.message;

import com.ezhixuan.xuanblog_backend.domain.dto.MessageDTO;
import com.ezhixuan.xuanblog_backend.domain.enums.MessageTypeEnum;

public interface MessageManager {

    /**
     * 获取消息类型
     * @author Ezhixuan
     * @return 实现者所属消息类型
     */
    MessageTypeEnum getMessageTypeEnum();

    /**
     * 消息发送唯一入口
     * @param messageDTO 消息 dto
     */
    void doSend(MessageDTO messageDTO);

    default void validate(MessageDTO messageDTO) {

    }
}
