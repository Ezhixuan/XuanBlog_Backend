package com.ezhixuan.blog.controller.upload;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.handler.picture.PictureUploadVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.handler.picture.PictureUploadDTO;
import com.ezhixuan.blog.service.SysPictureService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/pic")
@RestController
public class PictureController {

    final SysPictureService pictureService;

    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile file, PictureUploadDTO uploadDTO) {
        return R.success(pictureService.doUpload(file, uploadDTO));
    }

    @GetMapping("/list")
    public BaseResponse<PageResponse<PictureUploadVO>> getPictureList(
            @RequestParam(value = "type", required = false) Integer type) {
        return R.list(pictureService.getPictureVOList(type));
    }
}
