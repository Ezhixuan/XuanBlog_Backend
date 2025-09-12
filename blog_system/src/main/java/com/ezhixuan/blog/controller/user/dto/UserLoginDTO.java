package com.ezhixuan.blog.controller.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "用户登录信息")
@Data
public class UserLoginDTO {

    @Schema(description = "用户账号")
    @NotNull(message = "用户账号不能为空")
    @Size(min = 6, max = 18, message = "用户账号长度必须在6-18个字符之间")
    protected String account;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6-30个字符之间")
    protected String password;

    /**
     * 校验登录信息是否符合要求
     * 如果合规会生成加密后的密码
     * @author Ezhixuan
     * @return boolean
     * @throws com.ezhixuan.blog.exception.BusinessException
     */
    public boolean check() {
        if (account == null || password == null) {
            throw new RuntimeException("参数错误");
        }

        int accountLength = this.account.length();
        if (accountLength < 6 || accountLength > 18) {
            throw new RuntimeException("用户账号长度必须在6-18个字符之间");
        }

        int passwordLength = this.password.length();
        if (passwordLength < 6 || passwordLength > 30) {
            throw new RuntimeException("密码长度必须在6-30个字符之间");
        }

        return true;
    }
}
