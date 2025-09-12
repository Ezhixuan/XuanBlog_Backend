package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.dto.PictureQueryDTO;
import com.ezhixuan.blog.domain.entity.SysPicture;
import com.ezhixuan.blog.handler.picture.PictureUploadVO;
import com.ezhixuan.blog.handler.picture.UploadModel;
import org.springframework.web.multipart.MultipartFile;

import com.ezhixuan.blog.handler.picture.PictureUploadDTO;

import java.util.List;

public interface SysPictureService extends IService<SysPicture> {

    boolean register(String model);

    List<UploadModel> getAvailableType();

    String doUpload(MultipartFile file, PictureUploadDTO uploadDTO);

    IPage<PictureUploadVO> getPictureVOList(PictureQueryDTO queryDTO);
}
