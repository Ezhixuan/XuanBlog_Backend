package com.ezhixuan.xuanblog_backend.factory;

import java.io.IOException;
import java.io.InputStream;

import com.ezhixuan.xuanblog_backend.configs.propertites.UploadModelEnum;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * 上传接口
 *
 * @author ezhixuan
 */
public interface PictureManager {

    /**
     * 获取上传模型
     *
     * @author Ezhixuan
     * @return 实现者所属上传模型
     */
    UploadModelEnum getUploadModelEnum();

    /**
     * 上传图片
     * @author Ezhixuan
     * @param inputStream 文件流
     * @param targetPath 目标路径
     * @param fileName 文件名
     * @return url
     */
    String doUpload(InputStream inputStream, String targetPath, String fileName) throws IOException, UnirestException;

    /**
     * 下载文件
     *
     * @author Ezhixuan
     * @param urlStr
     * @return 文件流
     */
    InputStream doDownload(String urlStr);

}
