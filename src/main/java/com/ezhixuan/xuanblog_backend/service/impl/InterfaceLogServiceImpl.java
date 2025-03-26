package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.InterfaceLog;
import com.ezhixuan.xuanblog_backend.mapper.InterfaceLogMapper;
import com.ezhixuan.xuanblog_backend.service.InterfaceLogService;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【interface_log(接口调用日志表)】的数据库操作Service实现
* @createDate 2025-03-26 09:16:24
*/
@Service
public class InterfaceLogServiceImpl extends ServiceImpl<InterfaceLogMapper, InterfaceLog>
    implements InterfaceLogService {

}




