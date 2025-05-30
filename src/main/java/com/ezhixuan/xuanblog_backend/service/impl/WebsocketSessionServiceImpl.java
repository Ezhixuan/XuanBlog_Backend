package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.message.WebsocketSession;
import com.ezhixuan.xuanblog_backend.service.WebsocketSessionService;
import com.ezhixuan.xuanblog_backend.mapper.WebsocketSessionMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【websocket_session(WebSocket会话表)】的数据库操作Service实现
* @createDate 2025-05-30 14:07:52
*/
@Service
public class WebsocketSessionServiceImpl extends ServiceImpl<WebsocketSessionMapper, WebsocketSession>
    implements WebsocketSessionService{

}




