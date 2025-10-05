package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.log.dto.ReleaseLogQueryDTO;
import com.ezhixuan.blog.domain.entity.ReleaseLog;

/**
 * @author ezhixuan
 * @description 针对表【release_log(博客系统功能更新日志)】的数据库操作Service
 * @createDate 2025-09-29 22:26:37
 */
public interface ReleaseLogService extends IService<ReleaseLog> {

  /**
   * 分页查询发布日志列表，按创建时间倒序排序
   *
   * @param queryDTO 查询条件
   * @return 分页结果
   */
  IPage<ReleaseLog> pageReleaseLogList(ReleaseLogQueryDTO queryDTO);
}
