package com.ezhixuan.xuanblog_backend.controller;

import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.domain.vo.PictureUploadVO;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/list")
    public BaseResponse<PageResponse<PictureUploadVO>> getPictureList() {
        return R.list(pictureService.getPictureVOList());
    }
}
