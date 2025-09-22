package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.dto.ProjectArticleDocArtDTO;
import com.ezhixuan.blog.domain.entity.ProjDocArt;

/**
 * @author ezhixuan
 * @description 针对表【proj_doc_art(项目文档关联文章)】的数据库操作Service
 * @createDate 2025-09-18 14:40:51
 */
public interface ProjDocArtService extends IService<ProjDocArt> {

  /**
   * 移动文章到默认文档
   *
   * @param articleDocArtDTO 文章 id
   * @return 移动结果
   */
  Boolean moveToDefaultDco(ProjectArticleDocArtDTO articleDocArtDTO);

  /**
   * 链接文章到文档
   *
   * @param docId 文档 id
   * @param articleDocArtDTO 文章id 及排序信息
   */
  void link(Long docId, ProjectArticleDocArtDTO articleDocArtDTO);
}
