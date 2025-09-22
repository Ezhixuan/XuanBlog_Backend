package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.dto.ProjectDocCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocQueryDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocUpdateDTO;
import com.ezhixuan.blog.domain.entity.ProjectDoc;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ProjectDocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/docs")
@Tag(name = "ProjectDocController", description = "项目文档接口")
public class ProjectDocController {

    private final ProjectDocService projectDocService;

    @PostMapping
    @Operation(summary = "创建项目文档")
    public BaseResponse<ProjectDoc> createProjectDoc(@RequestBody ProjectDocCreateDTO createDTO) {
        return R.success(projectDocService.create(createDTO));
    }

    @PutMapping
    @Operation(summary = "更新项目文档")
    public BaseResponse<ProjectDoc> updateProjectDoc(@RequestBody ProjectDocUpdateDTO updateDTO) {
        return R.success(projectDocService.update(updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目文档")
    public BaseResponse<Boolean> deleteProjectDoc(@PathVariable Long id) {
        return R.success(projectDocService.deleteById(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取项目文档详情")
    public BaseResponse<ProjectDoc> getProjectDoc(@PathVariable Long id) {
        return R.success(projectDocService.getById(id));
    }

    @GetMapping
    @Operation(summary = "分页查询项目文档")
    public PageResponse<ProjectDoc> getProjectDocList(ProjectDocQueryDTO queryDTO) {
        return R.list(projectDocService.pageByDTO(queryDTO));
    }
}
