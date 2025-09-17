package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezhixuan.blog.controller.dto.ProjectQueryDTO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;
import com.ezhixuan.blog.domain.entity.Article;
import com.ezhixuan.blog.domain.entity.Project;
import com.ezhixuan.blog.domain.entity.Technology;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.ProjectQueryService;
import com.ezhixuan.blog.service.ProjectService;
import com.ezhixuan.blog.service.ProjectTechnologyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 项目统一查询服务实现
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Service
public class ProjectQueryServiceImpl implements ProjectQueryService {

  @Resource private ProjectService projectService;
  @Resource private ProjectTechnologyService projectTechnologyService;
  @Resource private ArticleService articleService;

  /**
   * 查询项目列表
   *
   * @param queryDTO 查询参数
   * @return ProjectQueryVO 项目列表
   */
  @Override
  public IPage<ProjectQueryVO> pageProjectListByDTO(ProjectQueryDTO queryDTO) {
    if (Objects.isNull(queryDTO)) {
      return new Page<>();
    }
    // 如果有指定技术栈
    if (nonNull(queryDTO.getTechnologyId())) {
      List<Long> projectIds = projectTechnologyService.queryProjectId(queryDTO.getTechnologyId());
      queryDTO.getProjectIds().addAll(projectIds);
    }
    IPage<Project> projectIPage = projectService.queryListByDTO(queryDTO);
    // 数据转换
    List<ProjectQueryVO> converted = convertToPageVO(projectIPage.getRecords());
    Page<ProjectQueryVO> projectQueryVOPage =
        new Page<>(projectIPage.getCurrent(), projectIPage.getSize(), projectIPage.getTotal());
    projectQueryVOPage.setRecords(converted);
    return projectQueryVOPage;
  }

  /**
   * 将Project实体转换为ProjectQueryVO
   *
   * @param project 项目实体
   * @return ProjectQueryVO 项目查询视图对象
   */
  @Override
  public ProjectQueryVO convertToPageVO(Project project) {
    return convertToPageVO(Collections.singletonList(project)).getFirst();
  }

  /**
   * 将Project实体列表转换为ProjectQueryVO列表
   *
   * @param projectList 项目实体列表
   * @return ProjectQueryVO 项目查询视图对象列表
   */
  @Override
  public List<ProjectQueryVO> convertToPageVO(List<Project> projectList) {
    if (isEmpty(projectList)) {
      return Collections.emptyList();
    }
    List<ProjectQueryVO> projectQueryVOS = projectList.stream().map(ProjectQueryVO::new).toList();
    // 获取项目 id
    List<Long> projectIds = projectQueryVOS.stream().map(ProjectQueryVO::getId).toList();
    Map<Long, List<Technology>> projectIdToTechnologyMap =
        projectTechnologyService.queryLink(projectIds);
    Map<Long, Boolean> projectIdToHasArticleMap = hasArticle(projectIds);
    // 补充数据
    projectQueryVOS.forEach(
        projectQueryVO -> {
          // todo start
          // todo view
          // 技术栈
          List<Technology> technologyList =
              projectIdToTechnologyMap.getOrDefault(
                  projectQueryVO.getId(), Collections.emptyList());
          List<String> nameList = technologyList.stream().map(Technology::getName).toList();
          projectQueryVO.setTechnologies(nameList);
          // 包含文章
          projectQueryVO.setHasArticles(
              projectIdToHasArticleMap.getOrDefault(projectQueryVO.getId(), false));
        });
    return null;
  }

  /**
   * 判断项目列表中是否包含文章
   *
   * @param projectIds 项目ID列表
   * @return 项目ID与是否包含文章的映射关系
   */
  private Map<Long, Boolean> hasArticle(List<Long> projectIds) {
    if (isEmpty(projectIds)) {
      return Map.of();
    }
    List<Article> articleList =
        articleService.list(Wrappers.<Article>lambdaQuery().in(Article::getProjectId, projectIds));
    Map<Long, Boolean> result = new HashMap<>();
    projectIds.forEach(id -> result.put(id, false));
    articleList.stream().map(Article::getProjectId).distinct().forEach(id -> result.put(id, true));
    return result;
  }
}
