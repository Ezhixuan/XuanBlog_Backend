package com.ezhixuan.blog.controller.user.dto;

import com.ezhixuan.blog.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息编辑 DTO
 */
@Schema(description = "用户信息编辑DTO")
@Data
public class UserEditDTO {

    @Schema(description = "新密码")
    private String password;

    @Schema(description = "旧密码")
    private String oldPassword;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "个人简介")
    private String profile;

    @Schema(description = "邮箱")
    private String email;

    public User toEntity(Long id) {
        User user = new User();
        user.setId(id);
        user.setName(username);
        user.setAvatar(avatar);
        user.setProfile(profile);
        user.setEmail(email);
        return user;
    }
}
