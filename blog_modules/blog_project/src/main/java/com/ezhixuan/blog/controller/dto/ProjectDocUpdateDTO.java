package com.ezhixuan.blog.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "项目文档更新DTO")
public class ProjectDocUpdateDTO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "项目 FK")
    private Long projectId;

    @Schema(description = "文档标题")
    private String title;
}
