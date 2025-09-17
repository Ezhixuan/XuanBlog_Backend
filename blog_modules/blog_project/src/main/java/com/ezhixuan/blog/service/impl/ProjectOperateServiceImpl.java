package com.ezhixuan.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ezhixuan.blog.controller.dto.ProjectCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectEditDTO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;
import com.ezhixuan.blog.domain.entity.Project;
import com.ezhixuan.blog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectOperateServiceImpl implements ProjectOperateService {

  private final ProjectService itemService;
  private final TechnologyService technologyService;
  private final ProjectTechnologyService linkService;
  private final ProjectQueryService queryService;

  /**
   * 创建项目
   *
   * @param createDTO 创建参数
   * @return ProjectQueryVO 项目信息
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProjectQueryVO save(ProjectCreateDTO createDTO) {
    List<Long> technologiesIds = technologyService.saveAll(createDTO.getTechnologies());
    Project project = BeanUtil.copyProperties(createDTO, Project.class);
    project.setCreateTime(LocalDateTime.now());
    itemService.save(project);
    linkService.saveAll(project.getId(), technologiesIds);
    return queryService.convertToPageVO(project);
  }

  /**
   * 编辑项目
   *
   * @param editDTO 编辑参数
   * @return ProjectQueryVO 项目信息
   */
  @Override
  public ProjectQueryVO edit(ProjectEditDTO editDTO) {
    List<Long> technologiesIds = technologyService.saveAll(editDTO.getTechnologies());
    Project project = BeanUtil.copyProperties(editDTO, Project.class);
    itemService.updateById(project);
    linkService.saveAll(project.getId(), technologiesIds);
    return queryService.convertToPageVO(project);
  }

  /**
   * 推荐
   *
   * @param projectId 项目id
   * @return 推荐结果
   */
  @Override
  public boolean featured(Long projectId) {
    Project item = itemService.getById(projectId);
    if (Objects.isNull(item)) {
      return false;
    }
    boolean res = !item.getFeatured();
    item.setFeatured(res);
    itemService.updateById(item);
    return res;
  }

  /**
   * 删除
   *
   * @param projectId 项目id
   * @return 删除结果
   */
  @Override
  public Boolean removeById(Long projectId) {
    linkService.removeByProjectId(projectId);
    return itemService.removeById(projectId);
  }
}
