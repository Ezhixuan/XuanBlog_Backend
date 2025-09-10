package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.BaseResponse;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.ProjectCreateDTO;
import com.ezhixuan.blog.domain.dto.ProjectEditDTO;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.vo.ProjectLinkArticleVo;
import com.ezhixuan.blog.domain.vo.ProjectQueryVO;
import com.ezhixuan.blog.service.ProjectItemService;
import com.ezhixuan.blog.service.ProjectOperateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "ProjectController", description = "项目接口")
public class ProjectController {

  private final ProjectOperateService operateService;
  private final ProjectItemService projectItemService;

  @Operation(summary = "获取项目列表")
  @GetMapping
  public PageResponse<ProjectQueryVO> getProjectList(ProjectQueryDTO queryDTO) {
    return R.list(operateService.getList(queryDTO));
  }

  @Operation(summary = "创建项目")
  @PostMapping
  public BaseResponse<ProjectQueryVO> addProject(@RequestBody ProjectCreateDTO createDTO) {
    return R.success(operateService.save(createDTO));
  }

  @Operation(summary = "更新项目")
  @PutMapping
  public BaseResponse<ProjectQueryVO> updateProject(@RequestBody ProjectEditDTO editDTO) {
    return R.success(operateService.edit(editDTO));
  }

  @Operation(summary = "推荐")
  @PutMapping("/featured/{id}")
  public BaseResponse<Boolean> featuredProject(@PathVariable Long id) {
    return R.success(operateService.featured(id));
  }

  @Operation(summary = "删除")
  @DeleteMapping("/{id}")
  public BaseResponse<Boolean> deleteProject(@PathVariable Long id) {
    return R.success(operateService.removeById(id));
  }

  @Operation(summary = "获取项目选择列表")
  @GetMapping("/link")
  public PageResponse<ProjectLinkArticleVo> getLinedArticleList() {
    return R.list(projectItemService.getLinkArticleList());
  }
}
