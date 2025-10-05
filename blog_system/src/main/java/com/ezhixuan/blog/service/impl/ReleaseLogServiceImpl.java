package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.log.dto.ReleaseLogQueryDTO;
import com.ezhixuan.blog.domain.entity.ReleaseLog;
import com.ezhixuan.blog.mapper.ReleaseLogMapper;
import com.ezhixuan.blog.service.ReleaseLogService;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

/**
 * @author ezhixuan
 * @description 针对表【release_log(博客系统功能更新日志)】的数据库操作Service实现
 * @createDate 2025-09-29 22:26:37
 */
@Service
public class ReleaseLogServiceImpl extends ServiceImpl<ReleaseLogMapper, ReleaseLog>
    implements ReleaseLogService {

  /**
   * 分页查询发布日志列表，按创建时间倒序排序
   *
   * @param queryDTO 查询条件
   * @return 分页结果
   */
  @Override
  public IPage<ReleaseLog> pageReleaseLogList(ReleaseLogQueryDTO queryDTO) {
    LambdaQueryWrapper<ReleaseLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(nonNull(queryDTO.getLevel()), ReleaseLog::getLevel, queryDTO.getLevel());
    queryWrapper.eq(nonNull(queryDTO.getStatus()), ReleaseLog::getStatus, queryDTO.getStatus());
    queryWrapper.eq(nonNull(queryDTO.getType()), ReleaseLog::getType, queryDTO.getType());
    queryWrapper.like(
        nonNull(queryDTO.getTitle()), ReleaseLog::getTitle, queryDTO.getTitle());
    queryWrapper.orderByDesc(ReleaseLog::getCreateTime);
    return page(queryDTO.toPage(), queryWrapper);
  }
}
