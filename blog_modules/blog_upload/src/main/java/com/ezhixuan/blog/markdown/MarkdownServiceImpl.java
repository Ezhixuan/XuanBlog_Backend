package com.ezhixuan.blog.markdown;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MarkdownServiceImpl implements MarkdownService {

    /*
     todo Ezhixuan : 目前只接受 Markdown 文件,后续可以考虑扩增
     */
    private static final String FILE_TYPE_X_WEB = "text/x-web-markdown";
    private static final String FILE_TYPE_X = "text/x-markdown";

    private boolean checkFileType(String fileType) {
        return FILE_TYPE_X.equals(fileType) || FILE_TYPE_X_WEB.equals(fileType);
    }

    /**
     * 上传 Markdown 文件
     *
     * @param file file 文件
     * @author Ezhixuan
     * @return 解析后的内容
     */
    @Override
    public String upload(MultipartFile file) {
        ThrowUtils.throwIf(Objects.isNull(file) || !checkFileType(file.getContentType()), ErrorCode.PARAMS_ERROR);
        // 读取文件
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("file inputStream error", e);
            ThrowUtils.throwIf(false, ErrorCode.SYSTEM_ERROR);
        }
        return null;
    }
}
