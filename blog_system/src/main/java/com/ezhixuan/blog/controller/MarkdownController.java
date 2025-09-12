package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.service.MarkdownService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/markdown")
@RequiredArgsConstructor
@Tag(name = "MarkdownController", description = "markdown上传")
public class MarkdownController {

  private final MarkdownService markdownService;

  @PostMapping("/upload")
  @Operation(summary = "上传 markdown")
  public BaseResponse<String> markdownUpload(
      @RequestPart("file") MultipartFile file,
      @RequestPart(value = "images", required = false) List<MultipartFile> images) {
    return R.success(markdownService.upload(file, images));
  }
}
