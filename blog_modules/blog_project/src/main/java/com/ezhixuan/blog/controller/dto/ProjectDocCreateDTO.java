package com.ezhixuan.blog.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "项目文档创建DTO")
public class ProjectDocCreateDTO {

    @Schema(description = "项目 FK")
    private Long projectId;

    @Schema(description = "文档标题")
    private String title;
}
