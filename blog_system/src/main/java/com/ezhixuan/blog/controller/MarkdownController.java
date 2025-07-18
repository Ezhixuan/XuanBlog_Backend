package com.ezhixuan.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.MarkdownService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/markdown")
@RequiredArgsConstructor
public class MarkdownController {

    private final MarkdownService markdownService;

    @Operation(summary = "上传 markdown")
    @PostMapping("/upload")
    public BaseResponse<String> markdownUpload(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        String content = markdownService.upload(file, images);
        return new BaseResponse<String>(0, content);
    }
}
