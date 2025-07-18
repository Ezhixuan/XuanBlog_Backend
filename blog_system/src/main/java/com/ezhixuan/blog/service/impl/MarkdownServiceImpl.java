package com.ezhixuan.blog.service.impl;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.CollectionUtils.newHashMap;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.handler.picture.PictureUploadDTO;
import com.ezhixuan.blog.service.MarkdownService;
import com.ezhixuan.blog.service.SysPictureService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarkdownServiceImpl implements MarkdownService {

    private final SysPictureService pictureService;

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
     * @param images
     * @return 解析后的内容
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile file, List<MultipartFile> images) {
        ThrowUtils.throwIf(Objects.isNull(file) || !checkFileType(file.getContentType()), ErrorCode.PARAMS_ERROR);
        String content = readContent(file);
        if (isEmpty(images)) {
            return content;
        }
        Map<String, String> fileNameToUrlMap = doUpload(images, new PictureUploadDTO());
        return replaceImages(content, fileNameToUrlMap);
    }

    private String replaceImages(String content, Map<String, String> fileNameToUrlMap) {
        // 这个正则表达式会匹配如 "![image1.png]" 并捕获 "image1.png"
        // !\[ 表示匹配 ![
        // (.*?) 表示非贪婪匹配任意字符，作为捕获组 1
        // \] 表示匹配 ]
        Pattern placeholderPattern = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)");
        Matcher matcher = placeholderPattern.matcher(content);

        // 查找、匹配、替换
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String altText = matcher.group(1);     // alt text, 例如 "Docker 架构图"
            String localPath = matcher.group(2);   // 本地路径, 例如 "/Users/.../image-20250713.png"
            String fileName = Paths.get(localPath).getFileName().toString();
            String url = fileNameToUrlMap.get(fileName);

            if (url != null) {
                String replacement = "![" + altText + "](" + url + ")";
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Map<String, String> doUpload(List<MultipartFile> images, PictureUploadDTO uploadDTO) {
        if (isEmpty(images)) {
            return newHashMap(0);
        }
        return images.stream().collect(Collectors.toMap(image -> {
                    String[] split = Objects.requireNonNull(image.getOriginalFilename()).split("/");
                    String name = split[split.length - 1];
                    return name.substring(0, name.lastIndexOf("."));
                },
            image -> pictureService.doUpload(image, uploadDTO), (exist, replace) -> replace));
    }

    @SneakyThrows
    private String readContent(MultipartFile file) {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}
