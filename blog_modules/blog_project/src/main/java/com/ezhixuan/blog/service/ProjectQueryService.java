package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.controller.dto.ProjectQueryDTO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;
import com.ezhixuan.blog.domain.entity.Project;

import java.util.List;

/**
 * 项目统一查询服务接口
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
public interface ProjectQueryService {

  /**
   * 查询项目列表
   *
   * @param queryDTO 查询参数
   * @return ProjectQueryVO 项目列表
   */
  IPage<ProjectQueryVO> pageProjectListByDTO(ProjectQueryDTO queryDTO);

  /**
   * 将Project实体转换为ProjectQueryVO
   *
   * @param project 项目实体
   * @return ProjectQueryVO 项目查询视图对象
   */
  ProjectQueryVO convertToPageVO(Project project);

  /**
   * 将Project实体列表转换为ProjectQueryVO列表
   *
   * @param projectList 项目实体列表
   * @return ProjectQueryVO 项目查询视图对象列表
   */
  List<ProjectQueryVO> convertToPageVO(List<Project> projectList);
}
