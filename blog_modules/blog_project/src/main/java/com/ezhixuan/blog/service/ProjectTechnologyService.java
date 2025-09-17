package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.ProjectTechnology;
import com.ezhixuan.blog.domain.entity.Technology;
import java.util.List;
import java.util.Map;

/**
 * @author ezhixuan
 * @description 针对表【link_project_technology】的数据库操作Service
 * @createDate 2025-07-06 01:36:07
 */
public interface ProjectTechnologyService extends IService<ProjectTechnology> {

  /**
   * 获取技术栈 id
   *
   * @author Ezhixuan
   * @param projectId 项目 id
   * @return List<Long> 技术栈 id
   */
  List<Long> queryLink(Long projectId);

  /**
   * 获取项目 id
   *
   * @author Ezhixuan
   * @param technologyId 技术栈 id
   * @return List<Long> 项目 id
   */
  List<Long> queryProjectId(Long technologyId);

  /**
   * 建立连接
   *
   * @author Ezhixuan
   * @param projectId 项目 id
   * @param technologiesIds 技术栈 id
   */
  void saveAll(Long projectId, List<Long> technologiesIds);

  /**
   * 断开链接
   *
   * @author Ezhixuan
   * @param projectId 项目 id
   */
  void removeByProjectId(Long projectId);

  /**
   * 通过项目id 列表获取关联的技术栈列表
   *
   * @param projectIds 项目 id 列表
   * @since 0.0.2beta
   * @return Map<Long,List<Technology>> 项目 id -> 技术栈列表
   */
  Map<Long, List<Technology>> queryLink(List<Long> projectIds);
}
