package com.ezhixuan.blog.handler.picture;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传接口
 *
 * @author ezhixuan
 */
public interface PictureUploadHandler {

    /**
     * 获取上传模型
     *
     * @author Ezhixuan
     * @return 实现者所属上传模型
     */
    UploadModel getUploadModel();

    /**
     * 上传图片
     * @author Ezhixuan
     * @param inputStream 文件流
     * @param targetPath 目标路径
     * @param fileName 文件名
     * @return url
     */
    String doUpload(InputStream inputStream, String targetPath, String fileName) throws IOException;

    /**
     * 下载文件
     *
     * @author Ezhixuan
     * @param urlStr
     * @return 文件流
     */
    InputStream doDownload(String urlStr);

    /**
     * 优先级 默认 100 越小越优先执行
     */
    default int getOrder() {
        return 100;
    }

    String model = "default";

}
