package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.ProjectTechnology;
import com.ezhixuan.blog.domain.entity.Technology;
import com.ezhixuan.blog.mapper.ProjectTechnologyMapper;
import com.ezhixuan.blog.service.ProjectTechnologyService;
import com.ezhixuan.blog.service.TechnologyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【link_project_technology】的数据库操作Service实现
 * @createDate 2025-07-06 01:36:07
 */
@Service
public class ProjectTechnologyServiceImpl
    extends ServiceImpl<ProjectTechnologyMapper, ProjectTechnology>
    implements ProjectTechnologyService {

  @Resource private TechnologyService technologyService;

  /**
   * 获取技术栈 id
   *
   * @param projectId 项目 id
   * @return List<Long> 技术栈 id
   * @author Ezhixuan
   */
  @Override
  public List<Long> queryLink(Long projectId) {
    if (isNull(projectId)) {
      return Collections.emptyList();
        }
    return listObjs(
        Wrappers.<ProjectTechnology>lambdaQuery()
            .select(ProjectTechnology::getTechnologyId)
            .eq(ProjectTechnology::getProjectId, projectId));
    }

  /**
   * 获取项目 id
   *
   * @param technologyId 技术栈 id
   * @return id 项目 id
   * @author Ezhixuan
   */
  @Override
  public List<Long> queryProjectId(Long technologyId) {
    if (isNull(technologyId)) {
      return Collections.emptyList();
        }
    return listObjs(
        Wrappers.<ProjectTechnology>lambdaQuery()
            .select(ProjectTechnology::getProjectId)
            .eq(ProjectTechnology::getTechnologyId, technologyId));
    }

  /**
   * 建立连接
   *
   * @param projectId 项目 id
   * @param technologiesIds 技术栈 id
   * @author Ezhixuan
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveAll(Long projectId, List<Long> technologiesIds) {
    List<ProjectTechnology> list =
        list(
            Wrappers.<ProjectTechnology>lambdaQuery()
                .eq(ProjectTechnology::getProjectId, projectId));
    if (!isEmpty(list)) {
      removeBatchByIds(list);
        }
    List<ProjectTechnology> projectTechnologyList =
        technologiesIds.stream()
            .map(
                technologyId -> {
                  ProjectTechnology link = new ProjectTechnology();
                  link.setProjectId(projectId);
                  link.setTechnologyId(technologyId);
                  return link;
                })
            .toList();
    saveBatch(projectTechnologyList);
  }

  /**
   * 断开链接
   *
   * @param projectId 项目 id
   * @author Ezhixuan
   */
  @Override
  public void removeByProjectId(Long projectId) {
    remove(
        Wrappers.<ProjectTechnology>lambdaQuery().eq(ProjectTechnology::getProjectId, projectId));
    }

  /**
   * 通过项目id 列表获取关联的技术栈列表
   *
   * @param projectIds 项目 id 列表
   * @return Map<Long,List<Technology>> 项目 id -> 技术栈列表
   * @since 0.0.2beta
   */
  @Override
  public Map<Long, List<Technology>> queryLink(List<Long> projectIds) {
    if (isEmpty(projectIds)) {
      return Map.of();
    }
    List<ProjectTechnology> linkedList =
        list(
            Wrappers.<ProjectTechnology>lambdaQuery()
                .in(ProjectTechnology::getProjectId, projectIds));
    if (isEmpty(linkedList)) {
      return Map.of();
    }
    // 获取对应的 Technology 信息
    List<Long> technologyIds = linkedList.stream().map(ProjectTechnology::getTechnologyId).toList();
    List<Technology> technologyList = technologyService.listByIds(technologyIds);
    if (isEmpty(technologyList)) {
      return Map.of();
    }
    return linkedList.stream()
        .collect(
            Collectors.groupingBy(
                ProjectTechnology::getProjectId,
                Collectors.mapping(ProjectTechnology::getTechnologyId, Collectors.toList())))
        .entrySet()
        .stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    technologyList.stream()
                        .filter(technology -> entry.getValue().contains(technology.getId()))
                        .toList()));
  }
}
