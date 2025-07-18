package com.ezhixuan.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.handler.picture.PictureUploadDTO;
import com.ezhixuan.blog.handler.picture.PictureUploadVO;
import com.ezhixuan.blog.handler.picture.UploadModel;
import com.ezhixuan.blog.service.SysPictureService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PictureUploadController {

    private final SysPictureService pictureService;

    @Operation(summary = "服务注册")
    @PostMapping("/register")
    public BaseResponse<Boolean> register(@RequestBody String Model) {
        return R.success(pictureService.register(Model));
    }

    @Operation(summary = "服务列表")
    @GetMapping("/service/list")
    public BaseResponse<List<UploadModel>> getPicServiceList() {
        return R.success(pictureService.getAvailableType());
    }

    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile file, PictureUploadDTO uploadDTO) {
        return R.success(pictureService.doUpload(file, uploadDTO));
    }

    @Operation(summary = "获取图片列表")
    @GetMapping("/list")
    public BaseResponse<PageResponse<PictureUploadVO>> getPictureList(
            @RequestParam(value = "type", required = false) Integer type) {
        return R.list(pictureService.getPictureVOList(type));
    }

    @Operation(summary = "删除图片")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deletePicture(@PathVariable Long id) {
        return R.success(pictureService.removeById(id));
    }

}
