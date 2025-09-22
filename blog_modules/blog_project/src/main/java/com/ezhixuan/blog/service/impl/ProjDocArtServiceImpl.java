package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.dto.ProjectArticleDocArtDTO;
import com.ezhixuan.blog.domain.entity.ProjDocArt;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.mapper.ProjDocArtMapper;
import com.ezhixuan.blog.service.ProjDocArtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * @author ezhixuan
 * @description 针对表【proj_doc_art(项目文档关联文章)】的数据库操作Service实现
 * @createDate 2025-09-18 14:40:51
 */
@Service
public class ProjDocArtServiceImpl extends ServiceImpl<ProjDocArtMapper, ProjDocArt>
    implements ProjDocArtService {

  /**
   * 移动文章到默认文档
   *
   * @param articleDocArtDTO 文章 id
   * @return 移动结果
   */
  @Override
  public Boolean moveToDefaultDco(ProjectArticleDocArtDTO articleDocArtDTO) {
    Long articleId = articleDocArtDTO.getArticleId();
    if (isNull(articleId)) {
      return true;
    }
    return remove(Wrappers.<ProjDocArt>lambdaQuery().eq(ProjDocArt::getArticleId, articleId));
  }

  /**
   * 链接文章到文档
   *
   * @param docId 文档 id
   * @param articleDocArtDTO 文章id 及排序信息
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void link(Long docId, ProjectArticleDocArtDTO articleDocArtDTO) {
    if (isNull(docId) || isNull(articleDocArtDTO)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
    }
    Long articleId = articleDocArtDTO.getArticleId();
    if (isNull(articleId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "未指定文章");
    }
    List<ProjDocArt> docArtList =
        list(Wrappers.<ProjDocArt>lambdaQuery().eq(ProjDocArt::getDocId, docId));
    Integer sortOrder = articleDocArtDTO.getSortOrder();
    sortOrder = isNull(sortOrder) ? (docArtList.size() + 1) * 10 : sortOrder;
    ProjDocArt projDocArt = new ProjDocArt();
    // 查询是否已存在关联
      ProjDocArt existingLink = getOne(
              Wrappers.<ProjDocArt>lambdaQuery()
                      .eq(ProjDocArt::getDocId, docId)
                      .eq(ProjDocArt::getArticleId, articleId)
      );
      if (isNull(existingLink)) {
          projDocArt.setDocId(docId);
          projDocArt.setArticleId(articleDocArtDTO.getArticleId());
          projDocArt.setSortOrder(sortOrder);
      } else {
          projDocArt = existingLink;
      }

    // 获取之前的所有数据进行排序
    docArtList.add(projDocArt);
    List<ProjDocArt> sortedList =
        docArtList.stream().sorted(Comparator.comparingInt(ProjDocArt::getSortOrder)).toList();
    List<ProjDocArt> needUpdateList = new ArrayList<>();
    for (int i = 0; i < sortedList.size(); i++) {
      ProjDocArt projDocArtInForLoop = sortedList.get(i);
      if (projDocArtInForLoop.getSortOrder() != i * 10) {
        projDocArtInForLoop.setSortOrder(i * 10);
        needUpdateList.add(projDocArtInForLoop);
      }
    }
    saveOrUpdateBatch(needUpdateList);
  }
}
