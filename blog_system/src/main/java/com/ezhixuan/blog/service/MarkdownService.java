package com.ezhixuan.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarkdownService {

    /**
     * 上传 Markdown 文件
     *
     * @param file   file 文件
     * @param images 图片列表
     * @return 解析后的内容
     * @author Ezhixuan
     */
    String upload(MultipartFile file, List<MultipartFile> images);
}
