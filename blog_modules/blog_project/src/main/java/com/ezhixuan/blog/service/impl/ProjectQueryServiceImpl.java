package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezhixuan.blog.controller.dto.ProjectQueryDTO;
import com.ezhixuan.blog.controller.vo.ProjectArticleDocVO;
import com.ezhixuan.blog.controller.vo.ProjectDocVO;
import com.ezhixuan.blog.controller.vo.ProjectQueryVO;
import com.ezhixuan.blog.domain.entity.*;
import com.ezhixuan.blog.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
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
  @Resource private ProjectDocService projectDocService;
  @Resource private ProjDocArtService projectDocArtService;
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
      return emptyList();
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
              projectIdToTechnologyMap.getOrDefault(projectQueryVO.getId(), emptyList());
          List<String> nameList = technologyList.stream().map(Technology::getName).toList();
          projectQueryVO.setTechnologies(nameList);
          // 包含文章
          projectQueryVO.setHasArticles(
              projectIdToHasArticleMap.getOrDefault(projectQueryVO.getId(), false));
        });
    return projectQueryVOS;
  }

  /**
   * 获取项目文章列表
   *
   * @param projectId 项目ID
   * @return ProjectDocVO 项目文章列表
   */
  @Override
  public List<ProjectDocVO> getProjectArticleDocList(Long projectId) {
    if (isNull(projectId)) {
      return emptyList();
    }
    // 获取项目下的所有文章信息
    List<Article> articleList =
        articleService.list(
            Wrappers.<Article>lambdaQuery()
                .eq(Article::getProjectId, projectId)
                .orderByAsc(Article::getCreateTime));
    if (isEmpty(articleList)) {
      return emptyList();
    }
    List<ProjectDoc> docList =
        projectDocService.list(
            Wrappers.<ProjectDoc>lambdaQuery().eq(ProjectDoc::getProjectId, projectId));
    // 获取 docIds
    if (isEmpty(docList)) {
      return getDefaultDocVO(articleList);
    }
    List<Long> articleIds = articleList.stream().map(Article::getId).toList();
    List<ProjDocArt> artList = projectDocArtService.listByIds(articleIds);

    // 将 articleList 转换成 map
    Map<Long, Article> articleIdToArticleMap =
        articleList.stream().collect(Collectors.toMap(Article::getId, Function.identity()));
    // 将 docList 转换成 map
    Map<Long, ProjectDoc> projectDocIdToDocMap =
        docList.stream().collect(Collectors.toMap(ProjectDoc::getId, Function.identity()));

    return getDocVo(artList, articleIdToArticleMap, projectDocIdToDocMap);
  }

  private List<ProjectDocVO> getDocVo(
      List<ProjDocArt> artList,
      Map<Long, Article> articleIdToArticleMap,
      Map<Long, ProjectDoc> projectDocIdToDocMap) {
    if (isEmpty(artList)) {
      // 如果 artList 为空那么需要构建 doc 后再补一个默认的
      List<ProjectDocVO> projectDocVOList =
          new ArrayList<>(
              projectDocIdToDocMap.values().stream()
                  .map(
                      projectDoc -> {
                        ProjectDocVO projectDocVO = new ProjectDocVO();
                        projectDocVO.setId(projectDoc.getId());
                        projectDocVO.setTitle(projectDoc.getTitle());
                        projectDocVO.setCreateTime(projectDoc.getCreateTime());
                        return projectDocVO;
                      })
                  .toList());
      projectDocVOList.addAll(getDefaultDocVO(articleIdToArticleMap.values().stream().toList()));
      return projectDocVOList;
    }

    List<ProjectDocVO> projectDocVOList =
        new ArrayList<>(
            artList.stream().collect(Collectors.groupingBy(ProjDocArt::getDocId)).values().stream()
                .map(
                    artStreamList -> {
                      ProjectDoc projectDoc =
                          projectDocIdToDocMap.get(artStreamList.getFirst().getDocId());
                      ProjectDocVO projectDocVO = new ProjectDocVO();
                      projectDocVO.setId(projectDoc.getId());
                      projectDocVO.setTitle(projectDoc.getTitle());
                      projectDocVO.setCreateTime(projectDoc.getCreateTime());
                      Map<Long, Integer> articleIdToSortOrderMap =
                          artStreamList.stream()
                              .collect(
                                  Collectors.toMap(
                                      ProjDocArt::getArticleId, ProjDocArt::getSortOrder));
                      Set<Long> articleIds = articleIdToSortOrderMap.keySet();
                      List<ProjectArticleDocVO> articleList =
                          articleIds.stream()
                              .map(
                                  articleId -> {
                                    ProjectArticleDocVO articleDocVO = new ProjectArticleDocVO();
                                    articleDocVO.setId(articleId);
                                    // 每次拿都是删除
                                    Article article = articleIdToArticleMap.remove(articleId);
                                    if (nonNull(article)) {
                                      articleDocVO.setTitle(article.getTitle());
                                    } else {
                                      return null;
                                    }
                                    articleDocVO.setSortOrder(
                                        articleIdToSortOrderMap.get(articleId));
                                    return articleDocVO;
                                  })
                              .filter(Objects::nonNull)
                              .toList();
                      projectDocVO.setArticles(articleList);
                      return projectDocVO;
                    })
                .toList());
    if (!isEmpty(articleIdToArticleMap)) {
      List<ProjectDocVO> defaultDcoVO =
          getDefaultDocVO(articleIdToArticleMap.values().stream().toList());
      projectDocVOList.addAll(defaultDcoVO);
    }
    return projectDocVOList;
  }

  private List<ProjectDocVO> getDefaultDocVO(List<Article> articleList) {
    ProjectDocVO projectDocVO = new ProjectDocVO();
    projectDocVO.setId(0L);
    projectDocVO.setTitle("项目文档");
    int sort = 0;
    List<ProjectArticleDocVO> articleDocVOList = new ArrayList<>(articleList.size());
    for (Article article : articleList) {
      ProjectArticleDocVO projectArticleDocVO = new ProjectArticleDocVO();
      projectArticleDocVO.setId(article.getId());
      projectArticleDocVO.setTitle(article.getTitle());
      projectArticleDocVO.setSortOrder(sort++);
      articleDocVOList.add(projectArticleDocVO);
    }
    projectDocVO.setArticles(articleDocVOList);
    return Collections.singletonList(projectDocVO);
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
