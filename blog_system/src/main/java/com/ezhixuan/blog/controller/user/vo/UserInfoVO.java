package com.ezhixuan.blog.controller.user.vo;

import com.ezhixuan.blog.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserInfoVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户名
     */
    private String name;

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
    private LocalDateTime createTime;

    /**
     * 用户角色
     */
    private String role;

    public UserInfoVO(User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.name = user.getName();
        this.avatar = user.getAvatar();
        this.profile = user.getProfile();
        this.email = user.getEmail();
        this.createTime = user. getCreateTime();
        this.role = user.getRole();
    }
}
