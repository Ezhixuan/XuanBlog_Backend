package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.message.MessageTask;
import com.ezhixuan.blog.service.MessageTaskService;
import com.ezhixuan.blog.mapper.MessageTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【message_task(消息任务表)】的数据库操作Service实现
* @createDate 2025-05-30 14:07:47
*/
@Service
public class MessageTaskServiceImpl extends ServiceImpl<MessageTaskMapper, MessageTask>
    implements MessageTaskService{

}




