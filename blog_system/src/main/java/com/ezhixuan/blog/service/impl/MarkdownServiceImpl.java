package com.ezhixuan.blog.service.impl;

import com.ezhixuan.blog.controller.picture.dto.PictureUploadDTO;
import com.ezhixuan.blog.service.MarkdownService;
import com.ezhixuan.blog.service.SysPictureService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.CollectionUtils.newHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarkdownServiceImpl implements MarkdownService {

    private final SysPictureService pictureService;

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
        if (isNull(file)) {
            return "";
        }
        String content = readContent(file);
        if (isEmpty(images)) {
            return content;
        }
        PictureUploadDTO pictureUploadDTO = new PictureUploadDTO();
        pictureUploadDTO.setReName(false);
        Map<String, String> fileNameToUrlMap = doUpload(images, pictureUploadDTO);
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
                    return split[split.length - 1];
                },
            image -> pictureService.doUpload(image, uploadDTO).getUrl()
        ));
    }

    @SneakyThrows
    private String readContent(MultipartFile file) {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}
