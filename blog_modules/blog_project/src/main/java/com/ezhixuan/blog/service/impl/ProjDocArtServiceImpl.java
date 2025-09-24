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

import java.util.Objects;

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
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    Long articleId = articleDocArtDTO.getArticleId();
    if (isNull(articleId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
      }

    lambdaQuery()
        .eq(ProjDocArt::getArticleId, articleId)
        .oneOpt()
        .ifPresent(
            projDocArt -> {
              // 如果存在
              if (Objects.equals(projDocArt.getDocId(), docId)
                  && (Objects.equals(projDocArt.getSortOrder(), articleDocArtDTO.getSortOrder() - 1)
                      || Objects.equals(
                          projDocArt.getSortOrder(), articleDocArtDTO.getSortOrder() + 1))) {
                throw new BusinessException(ErrorCode.SUCCESS);
              }
              removeById(articleId);
            });

    //link
    ProjDocArt docArt = new ProjDocArt();
    docArt.setArticleId(articleId);
    docArt.setDocId(docId);
    docArt.setSortOrder(articleDocArtDTO.getSortOrder());
    save(docArt);
    // async sort
      Thread.startVirtualThread(() -> {
          lambdaQuery()
                  .eq(ProjDocArt::getDocId, docId)
                  .orderByAsc(ProjDocArt::getSortOrder)
                  .last("FOR UPDATE");
          int rows = this.baseMapper.compactSortOrder(docId);
      });
  }
}
