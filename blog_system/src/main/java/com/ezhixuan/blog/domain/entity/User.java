package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ezhixuan.blog.domain.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@TableName("sys_user")
public class User {
  @Schema(description = "主键")
  private Long id;

  @Schema(description = "登录账号")
  private String account;

  @Schema(description = "用户昵称")
  private String name;

  @Schema(description = "bcrypt 密文")
  private String password;

  @Schema(description = "邮箱")
  private String email;

  @Schema(description = "头像 URL（或 picture.id）")
  private String avatar;

  @Schema(description = "个人简介")
  private String profile;

  @Schema(description = "0-禁用 1-正常")
  private Integer status;

  @Schema(description = "0-未删 1-已删")
  private Integer deleted;

  @Schema(description = "角色")
  private String role;

  @Schema(description = "用户注册时间")
  @TableField("create_time")
  private LocalDateTime createTime;

  @Schema(description = "上次修改信息时间")
  @TableField("update_time")
  private LocalDateTime updateTime;

  public User(String account, String encryptedPwd, String role) {
    this.account = account;
    this.name = account;
    this.password = encryptedPwd;
    this.role = role;
    if (Objects.equals(role, RoleEnum.ROLE_ADMIN.getRole())) {
      this.id = 1L;
    }
  }
}
