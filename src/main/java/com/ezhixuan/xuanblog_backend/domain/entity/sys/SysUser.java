package com.ezhixuan.xuanblog_backend.domain.entity.sys;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 头像地址
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 个人简介
     */
    @TableField(value = "profile")
    private String profile;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 状态：1-正常，0-禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 用户角色
     */
    @TableField(value = "role")
    private String role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
