package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.PictureQueryDTO;
import com.ezhixuan.blog.handler.picture.PictureUploadDTO;
import com.ezhixuan.blog.handler.picture.PictureUploadVO;
import com.ezhixuan.blog.handler.picture.UploadModel;
import com.ezhixuan.blog.service.SysPictureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
@Tag(name = "PictureUploadController", description = "图片上传")
public class PictureUploadController {

  private final SysPictureService pictureService;

  @PostMapping("/register")
  @Operation(summary = "服务注册")
  public BaseResponse<Boolean> register(@RequestBody String Model) {
    return R.success(pictureService.register(Model));
  }

  @GetMapping("/service")
  @Operation(summary = "服务列表")
  public BaseResponse<List<UploadModel>> getPicServiceList() {
    return R.success(pictureService.getAvailableType());
  }

  @PostMapping("/upload")
  @Operation(summary = "上传图片")
  public BaseResponse<String> upload(
      @RequestParam("file") MultipartFile file, PictureUploadDTO uploadDTO) {
    return R.success(pictureService.doUpload(file, uploadDTO));
  }

  @GetMapping
  @Operation(summary = "获取图片列表")
  public PageResponse<PictureUploadVO> getPictureList(PictureQueryDTO queryDTO) {
    return R.list(pictureService.getPictureVOList(queryDTO));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "删除图片")
  public BaseResponse<Boolean> deletePicture(@PathVariable Long id) {
    return R.success(pictureService.removeById(id));
  }
}
