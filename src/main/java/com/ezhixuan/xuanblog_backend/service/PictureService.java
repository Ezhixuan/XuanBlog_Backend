package com.ezhixuan.xuanblog_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.xuanblog_backend.domain.entity.sys.SysPicture;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.xuanblog_backend.domain.dto.PictureUploadDTO;

public interface PictureService extends IService<SysPicture> {

    String doUpload(MultipartFile file, PictureUploadDTO uploadDTO);
}
