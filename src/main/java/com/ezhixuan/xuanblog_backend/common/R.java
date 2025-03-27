package com.ezhixuan.xuanblog_backend.common;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;

public class R {

    /**
     * 成功
     *
     * @return 响应
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(0, null, "ok");
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 响应
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

    public static <T> BaseResponse<PageResponse<T>> list(List<T> data) {
        PageResponse<T> tPageResponse = new PageResponse<>();
        tPageResponse.setData(data);
        tPageResponse.setTotal(data.size());
        return success(tPageResponse);
    }

    public static <T> BaseResponse<PageResponse<T>> list(IPage<T> page) {
        PageResponse<T> tPageResponse = new PageResponse<>();
        tPageResponse.setData(page.getRecords());
        tPageResponse.setTotal(page.getTotal());
        return success(tPageResponse);
    }

}
