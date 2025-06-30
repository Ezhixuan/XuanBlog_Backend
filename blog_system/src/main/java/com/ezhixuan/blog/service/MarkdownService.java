package com.ezhixuan.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface MarkdownService {

    /**
     * 上传 Markdown 文件
     * @author Ezhixuan
     * @param file file 文件
     * @return 解析后的内容
     */
    String upload(MultipartFile file);
}
