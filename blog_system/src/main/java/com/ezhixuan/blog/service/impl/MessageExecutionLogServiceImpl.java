package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.message.MessageExecutionLog;
import com.ezhixuan.blog.service.MessageExecutionLogService;
import com.ezhixuan.blog.mapper.MessageExecutionLogMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【message_execution_log(消息执行日志表)】的数据库操作Service实现
* @createDate 2025-05-30 14:07:42
*/
@Service
public class MessageExecutionLogServiceImpl extends ServiceImpl<MessageExecutionLogMapper, MessageExecutionLog>
    implements MessageExecutionLogService{

}




