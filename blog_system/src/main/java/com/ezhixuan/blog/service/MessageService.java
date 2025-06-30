package com.ezhixuan.blog.service;

import java.util.List;

import com.ezhixuan.blog.domain.dto.MessageDTO;

public interface MessageService {

    /**
     * 发送消息
     * @author Ezhixuan
     * @param messageDTO 消息 dto
     */
    void sendMessage(MessageDTO messageDTO);

    /**
     * 异步发送消息
     * @author Ezhixuan
     * @param messageDTO 消息 dto
     */
    void sendMessageAsync(MessageDTO messageDTO);

    /**
     * 批量发送消息
     * @author Ezhixuan
     * @param messageDTOList 消息 dto 列表
     * @return 成功条数
     */
    int sendMessageBatch(List<MessageDTO> messageDTOList);
}
