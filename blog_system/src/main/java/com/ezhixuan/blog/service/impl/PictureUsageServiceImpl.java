package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.PictureUsage;
import com.ezhixuan.blog.domain.enums.PictureTypeEnum;
import com.ezhixuan.blog.mapper.PictureUsageMapper;
import com.ezhixuan.blog.service.PictureUsageService;
import com.ezhixuan.blog.service.SysPictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【picture_usage(图片使用关联表)】的数据库操作Service实现
 * @createDate 2025-09-13 12:04:11
 */
@Slf4j
@Service
public class PictureUsageServiceImpl extends ServiceImpl<PictureUsageMapper, PictureUsage>
    implements PictureUsageService {

  @Resource private SysPictureService pictureService;

  /**
   * 图片关联
   *
   * @param pictureType 图片类型
   * @param entityId 业务对象 ID
   * @param pictureIds 图片 ID
   * @return boolean 关联状态
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean link(PictureTypeEnum pictureType, Long entityId, List<Long> pictureIds) {
    // 参数校验
    if (isNull(pictureType) || isNull(entityId) || isEmpty(pictureIds)) {
      return false;
    }
    // 检查之前的关联关系, 如果存在关联
    List<Long> unLinkedPictureIds =
        list(
                Wrappers.<PictureUsage>lambdaQuery()
                    .eq(PictureUsage::getEntityType, pictureType.getValue())
                    .eq(PictureUsage::getEntityId, entityId))
            .stream()
            .map(PictureUsage::getPictureId)
            .filter(pictureId -> !pictureIds.contains(pictureId))
            .toList();
    unLink(pictureType, entityId, unLinkedPictureIds);
    // link
    return saveBatch(
        pictureIds.stream()
            .map(
                pictureId -> {
                  PictureUsage pictureUsage = new PictureUsage();
                  pictureUsage.setEntityType(pictureType.getValue());
                  pictureUsage.setEntityId(entityId);
                  pictureUsage.setPictureId(pictureId);
                  return pictureUsage;
                })
            .toList());
  }

  /**
   * 图片取消关联
   *
   * @param pictureType 图片类型
   * @param entityId 业务对象 ID
   * @param pictureIds 图片 ID 列表
   * @return boolean 取消关联状态
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean unLink(PictureTypeEnum pictureType, Long entityId, List<Long> pictureIds) {
    if (isNull(pictureType) || isNull(entityId) || isEmpty(pictureIds)) {
      return false;
    }
    List<PictureUsage> unLinkedPictureList =
        list(
            Wrappers.<PictureUsage>lambdaQuery()
                .eq(PictureUsage::getEntityType, pictureType.getValue())
                .eq(PictureUsage::getEntityId, entityId)
                .in(PictureUsage::getPictureId, pictureIds));
    removeBatchByIds(unLinkedPictureList);
    Thread.startVirtualThread(
        () -> {
          // 删除没被使用的图片
          List<PictureUsage> livedPictureList =
              list(Wrappers.<PictureUsage>lambdaQuery().in(PictureUsage::getPictureId, pictureIds));
          if (isEmpty(livedPictureList)) {
            return;
          }
          // 查询出经过删除后还有使用的图片 id
          List<Long> livedPictureIds =
              livedPictureList.stream().map(PictureUsage::getPictureId).toList();
          // 过滤出经过删除后没有被使用的图片 id
          List<Long> unLivedPictureIds =
              pictureIds.stream().filter(picId -> !livedPictureIds.contains(picId)).toList();
          if (isEmpty(unLivedPictureIds)) {
            return;
          }
          pictureService.deleteById(unLivedPictureIds);
        });
    return true;
  }
}
