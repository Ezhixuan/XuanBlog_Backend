package com.ezhixuan.blog.exception;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public SystemException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
