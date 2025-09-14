package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.PictureUsage;
import com.ezhixuan.blog.domain.enums.PictureTypeEnum;

import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【picture_usage(图片使用关联表)】的数据库操作Service
 * @createDate 2025-09-13 12:04:11
 */
public interface PictureUsageService extends IService<PictureUsage> {

  /**
   * 图片关联
   *
   * @param pictureType 图片类型
   * @param entityId 业务对象 ID
   * @param pictureIds 图片 ID 列表
   * @return boolean 关联状态
   */
  boolean link(PictureTypeEnum pictureType, Long entityId, List<Long> pictureIds);

  /**
   * 图片取消关联
   *
   * @param pictureType 图片类型
   * @param entityId 业务对象 ID
   * @param pictureIds 图片 ID 列表
   * @return boolean 取消关联状态
   */
  boolean unLink(PictureTypeEnum pictureType, Long entityId, List<Long> pictureIds);
}
