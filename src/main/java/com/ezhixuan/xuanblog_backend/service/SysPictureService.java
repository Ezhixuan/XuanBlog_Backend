package com.ezhixuan.xuanblog_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.xuanblog_backend.domain.entity.sys.SysPicture;
import com.ezhixuan.xuanblog_backend.domain.vo.PictureUploadVO;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.xuanblog_backend.domain.dto.PictureUploadDTO;

import java.util.List;

public interface SysPictureService extends IService<SysPicture> {

    String doUpload(MultipartFile file, PictureUploadDTO uploadDTO);

    List<PictureUploadVO> getPictureVOList();
}
