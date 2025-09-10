package com.ezhixuan.blog.common;

import com.ezhixuan.blog.exception.ErrorCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

  @Serial private static final long serialVersionUID = -5823962481561743284L;

  /** 消息 */
  private String message;

  /** 记录码 */
  private int code;

  /** 数据 */
  private T data;

  public BaseResponse(int code, T data, String message) {
    this.code = code;
    this.data = data;
    this.message = message;
  }

  public BaseResponse(int code, T data) {
    this(code, data, "ok");
  }

  public BaseResponse(ErrorCode errorCode) {
    this(errorCode.getCode(), null, errorCode.getMessage());
  }

  public BaseResponse(T data) {
    this(0, data);
  }

  public BaseResponse() {
    this(0, null, "ok");
  }
}
