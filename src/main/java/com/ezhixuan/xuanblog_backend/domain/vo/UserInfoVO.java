package com.ezhixuan.xuanblog_backend.domain.vo;

import java.util.Date;

import com.ezhixuan.xuanblog_backend.domain.entity.sys.SysUser;

import lombok.Data;

@Data
public class UserInfoVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户角色
     */
    private String role;

    public UserInfoVO(SysUser sysUser) {
        this.id = sysUser.getId();
        this.userAccount = sysUser.getUserAccount();
        this.username = sysUser.getUsername();
        this.avatar = sysUser.getAvatar();
        this.profile = sysUser.getProfile();
        this.email = sysUser.getEmail();
        this.createTime = sysUser.getCreateTime();
        this.role = sysUser.getRole();
    }
}
