package com.ezhixuan.xuanblog_backend.factory.message;

import org.springframework.stereotype.Service;

import com.ezhixuan.xuanblog_backend.domain.dto.MessageDTO;
import com.ezhixuan.xuanblog_backend.domain.enums.MessageTypeEnum;

@Service
public class WebsocketMessageServiceImpl implements MessageManager {
    /**
     * 获取消息类型
     *
     * @return 实现者所属消息类型
     * @author Ezhixuan
     */
    @Override
    public MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.WEBSOCKET;
    }

    /**
     * 消息发送唯一入口
     *
     * @param messageDTO 消息 dto
     */
    @Override
    public void doSend(MessageDTO messageDTO) {
        validate(messageDTO);


    }

    @Override
    public void validate(MessageDTO messageDTO) {
    }
}
