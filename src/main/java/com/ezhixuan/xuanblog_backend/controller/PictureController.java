package com.ezhixuan.xuanblog_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.PictureUploadDTO;
import com.ezhixuan.xuanblog_backend.service.PictureService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/pic")
@RestController
public class PictureController {

    final PictureService pictureService;

    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile file, PictureUploadDTO uploadDTO) {
        return R.success(pictureService.doUpload(file, uploadDTO));
    }
}
