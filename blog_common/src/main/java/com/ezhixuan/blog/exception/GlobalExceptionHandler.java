package com.ezhixuan.blog.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.ezhixuan.blog.entity.BaseResponse;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(NotLoginException e) {
        log.error("NotLoginException", e);
        return new BaseResponse<>(ErrorCode.NOT_LOGIN_ERROR, "未登录");
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return new BaseResponse<>(e.getCode(), null, e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public BaseResponse<?> systemExceptionHandler(SystemException e) {
        log.error("SystemException", e);
        return new BaseResponse<>(ErrorCode.SYSTEM_ERROR, "糟糕！发送了不可预料的问题，请联系管理员");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResponse<String> handleFileSizeLimitExceeded(MaxUploadSizeExceededException ex) {
        String message = "文件大小超过限制（最大允许20MB）";
        return new BaseResponse<>(ErrorCode.PARAMS_ERROR, message);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return new BaseResponse<>(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
