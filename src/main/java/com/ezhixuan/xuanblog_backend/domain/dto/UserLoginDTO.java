package com.ezhixuan.xuanblog_backend.domain.dto;

import java.util.Objects;

import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;

import lombok.Data;

/**
 * 用户登录 dto
 */
@Data
public class UserLoginDTO {

    /**
     * 用户账号
     */
    protected String userAccount;

    /**
     * 密码
     */
    protected String password;

    /**
     * 校验登录信息是否符合要求
     * 如果合规会生成加密后的密码
     * @author Ezhixuan
     * @return boolean
     * @throws com.ezhixuan.xuanblog_backend.exception.BusinessException
     */
    public boolean check() {
        ThrowUtils.throwIf(Objects.isNull(userAccount) || Objects.isNull(password), ErrorCode.PARAMS_ERROR);
        int length = this.userAccount.length();
        ThrowUtils.throwIf(length < 6 || length > 18, ErrorCode.PARAMS_ERROR);
        length = this.password.length();
        ThrowUtils.throwIf(length < 6 || length > 30, ErrorCode.PARAMS_ERROR);
        return true;
    }

}
