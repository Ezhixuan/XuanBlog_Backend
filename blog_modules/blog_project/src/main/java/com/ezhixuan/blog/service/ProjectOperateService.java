package com.ezhixuan.blog.service;

import com.ezhixuan.blog.controller.dto.ProjectArticleDocArtDTO;
import com.ezhixuan.blog.controller.dto.ProjectCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectEditDTO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;

public interface ProjectOperateService {

  /**
   * 创建项目
   *
   * @author Ezhixuan
   * @param createDTO 创建参数
   * @return ProjectQueryVO 项目信息
   */
  ProjectQueryVO save(ProjectCreateDTO createDTO);

  /**
   * 编辑项目
   *
   * @param editDTO 编辑参数
   * @return ProjectQueryVO 项目信息
   */
  ProjectQueryVO edit(ProjectEditDTO editDTO);

  /**
   * 推荐
   *
   * @param projectId 项目id
   * @return 是否成功
   */
  boolean featured(Long projectId);

  /**
   * 删除
   *
   * @param projectId 项目id
   * @return 是否成功
   */
  Boolean removeById(Long projectId);

  /**
   * 构建文章列表布局
   *
   * @param docId 文档id
   * @param articleDocVO 文章列表布局参数
   * @return 布局结果
   */
  Boolean buildArticleSort(Long docId, ProjectArticleDocArtDTO articleDocArtDTO);
}
