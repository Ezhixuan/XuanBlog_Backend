package com.ezhixuan.xuanblog_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.ResultUtils;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public BaseResponse<?> systemExceptionHandler(SystemException e) {
        log.error("SystemException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "糟糕！发送了不可预料的问题，请联系管理员");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleFileSizeLimitExceeded(MaxUploadSizeExceededException ex) {
        String message = "文件大小超过限制（最大允许20MB）";
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
