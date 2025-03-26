package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.SysUser;
import com.ezhixuan.xuanblog_backend.service.SysUserService;
import com.ezhixuan.xuanblog_backend.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-03-26 16:38:17
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




