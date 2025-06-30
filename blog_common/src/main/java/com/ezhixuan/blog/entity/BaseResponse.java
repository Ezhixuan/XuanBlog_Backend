package com.ezhixuan.blog.entity;

import java.io.Serializable;

import com.ezhixuan.blog.exception.ErrorCode;

import lombok.Data;

@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public BaseResponse(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), null, message);
    }
}
