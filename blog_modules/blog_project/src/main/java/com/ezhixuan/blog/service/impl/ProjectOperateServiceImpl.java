package com.ezhixuan.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ezhixuan.blog.controller.dto.ProjectArticleDocArtDTO;
import com.ezhixuan.blog.controller.dto.ProjectCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectEditDTO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;
import com.ezhixuan.blog.domain.entity.Project;
import com.ezhixuan.blog.domain.entity.ProjectDoc;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ProjectOperateServiceImpl implements ProjectOperateService {

  private final ProjectService projectService;
  private final TechnologyService technologyService;
  private final ProjectTechnologyService projectTechnologyService;
  private final ProjectDocService projectDocService;
  private final ProjDocArtService projectDocArtService;
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
    projectService.save(project);
    projectTechnologyService.saveAll(project.getId(), technologiesIds);
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
    projectService.updateById(project);
    projectTechnologyService.saveAll(project.getId(), technologiesIds);
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
    Project item = projectService.getById(projectId);
    if (isNull(item)) {
      return false;
    }
    boolean res = !item.getFeatured();
    item.setFeatured(res);
    projectService.updateById(item);
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
    projectTechnologyService.removeByProjectId(projectId);
    return projectService.removeById(projectId);
  }

  /**
   * 构建文章列表布局
   *
   * @param docId 文档id
   * @param articleDocVO 文章列表布局参数
   * @return 布局结果
   */
  @Override
  public Boolean buildArticleSort(Long docId, ProjectArticleDocArtDTO articleDocArtDTO) {
    if (isNull(docId) || docId == 0L) {
      return projectDocArtService.moveToDefaultDco(articleDocArtDTO);
    }
    ProjectDoc projectDoc = projectDocService.getById(docId);
    if (isNull(projectDoc)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文档分组不存在");
    }
    projectDocArtService.link(docId, articleDocArtDTO);
    return true;
  }
}
