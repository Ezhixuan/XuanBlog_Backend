package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "项目文档查询DTO")
public class ProjectDocQueryDTO extends PageRequest {

    @Schema(description = "项目 FK")
    private Long projectId;

    @Schema(description = "文档标题")
    private String title;
}
