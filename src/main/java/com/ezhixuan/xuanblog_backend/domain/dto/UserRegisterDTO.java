package com.ezhixuan.xuanblog_backend.domain.dto;

import java.util.Objects;

import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserLoginDTO{

    /**
     * 确认密码
     */
    private String confirmPassword;

    @Override
    public boolean check() {
        ThrowUtils.throwIf(!Objects.equals(password, confirmPassword), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        return super.check();
    }

}
