package com.ezhixuan.blog.service;

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
   * @author Ezhixuan
   * @param projectId 项目id
   */
  boolean featured(Long projectId);

  /**
   * 删除
   *
   * @author Ezhixuan
   * @param projectId 项目id
   */
  Boolean removeById(Long projectId);
}
