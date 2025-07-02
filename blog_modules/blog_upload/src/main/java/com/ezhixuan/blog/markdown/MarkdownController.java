package com.ezhixuan.blog.markdown;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.entity.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/markdown")
@RequiredArgsConstructor
public class MarkdownController {

    private final MarkdownService markdownService;

    @PostMapping("/upload")
    public BaseResponse<String> markdownUpload(@RequestPart("file") MultipartFile file) {
        String content = markdownService.upload(file);
        return new BaseResponse<String>(0, content);
    }
}
