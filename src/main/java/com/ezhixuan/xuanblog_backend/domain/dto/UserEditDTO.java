package com.ezhixuan.xuanblog_backend.domain.dto;

import lombok.Data;

/**
 * 用户信息编辑 DTO
 */
@Data
public class UserEditDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 密码
     */
    private String password;

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
}
