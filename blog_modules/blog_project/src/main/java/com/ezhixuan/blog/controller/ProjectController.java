package com.ezhixuan.blog.controller;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.ProjectCreateDTO;
import com.ezhixuan.blog.domain.dto.ProjectEditDTO;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.vo.ProjectQueryVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.ProjectOperateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "项目控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectOperateService operateService;

    @Operation(summary = "获取项目列表")
    @GetMapping
    public BaseResponse<PageResponse<ProjectQueryVO>> getList(ProjectQueryDTO queryDTO) {
        return R.list(operateService.getList(queryDTO));
    }

    @Operation(summary = "创建项目")
    @PostMapping
    public BaseResponse<ProjectQueryVO> create(@RequestBody ProjectCreateDTO createDTO) {
        return R.success(operateService.save(createDTO));
    }

    @Operation(summary = "更新项目")
    @PutMapping()
    public BaseResponse<ProjectQueryVO> update(@RequestBody ProjectEditDTO editDTO) {
        return R.success(operateService.edit(editDTO));
    }

    @Operation(summary = "推荐")
    @PutMapping("/featured/{id}")
    public BaseResponse<Void> featured(@PathVariable Long id) {
        operateService.featured(id);
        return R.success();
    }

}
