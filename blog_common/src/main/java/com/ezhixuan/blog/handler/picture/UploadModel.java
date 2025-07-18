package com.ezhixuan.blog.handler.picture;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ezhixuan
 * @version 1.0
 * @description: TODO
 * @date 2024/7/15 16:00
 */
@Getter
@AllArgsConstructor
public enum UploadModel {
    /**
     * 本地上传
     */
    LOCAL("local","本地上传"),
    /**
     * minio上传
     */
    MINIO("minio","minio上传"),
    /**
     * github上传
     */
    GITHUB("github","github上传"),
    ;


    private final String model;

    private final String desc;
}
