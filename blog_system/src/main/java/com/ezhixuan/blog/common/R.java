package com.ezhixuan.blog.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.exception.ErrorCode;
import java.util.List;

public class R {
  public static final BaseResponse<String> SUCCESS = new BaseResponse<>(ErrorCode.SUCCESS);
  public static final BaseResponse<String> ERROR = new BaseResponse<>(ErrorCode.SYSTEM_ERROR);
  public static final BaseResponse<String> PARAMS_ERROR =
      new BaseResponse<>(ErrorCode.PARAMS_ERROR);

  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(data);
  }

  public static <T> BaseResponse<T> success() {
    return new BaseResponse<>();
  }

  public static BaseResponse<String> error(int code, String message) {
    return new BaseResponse<>(code, null, message);
  }

  public static BaseResponse<String> error(ErrorCode errorCode, String message) {
    return new BaseResponse<>(errorCode.getCode(), null, message);
  }

  public static BaseResponse<String> error(ErrorCode errorCode) {
    return new BaseResponse<>(errorCode);
  }

  public static <T> PageResponse<T> list(List<T> list) {
    PageResVo<T> tPageResVo = new PageResVo<>(list);
    return new PageResponse<>(tPageResVo);
  }

  public static <T> PageResponse<T> list(IPage<T> tPage) {
    PageResVo<T> tPageResVo = new PageResVo<>(tPage);
    return new PageResponse<>(tPageResVo);
  }
}
