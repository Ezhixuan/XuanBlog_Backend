package com.ezhixuan.blog.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Schema(description = "用户注册DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserLoginDTO{

    /**
     * 确认密码
     */
    @Schema(description = "确认密码")
    @NotNull(message = "确认密码不能为空")
    protected String confirmPassword;

    @Override
    public boolean check() {
        if (!Objects.equals(password, confirmPassword)) {
            throw new RuntimeException("两次密码不一致");
        }
        return super.check();
    }
}
