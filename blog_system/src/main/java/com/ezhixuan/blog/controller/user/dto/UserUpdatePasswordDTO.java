package com.ezhixuan.blog.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "用户修改密码DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserUpdatePasswordDTO extends UserRegisterDTO{

    public UserUpdatePasswordDTO(String account, String password, String oldPassword) {
        this.account = account;
        this.password = password;
        this.confirmPassword = oldPassword;
    }
}
