package com.ezhixuan.xuanblog_backend.controller.upload;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.service.MarkdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.xuanblog_backend.common.R;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/md")
@RequiredArgsConstructor
public class MarkdownController {

    private final MarkdownService markdownService;

    @PostMapping("/upload")
    public BaseResponse<String> markdownUpload(@RequestPart("file") MultipartFile file) {
        /*
         todo Ezhixuan : 权限校验
         */
        String content = markdownService.upload(file);
        return R.success(content);
    }
}
