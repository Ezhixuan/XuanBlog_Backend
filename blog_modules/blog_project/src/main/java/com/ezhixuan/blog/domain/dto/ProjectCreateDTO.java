package com.ezhixuan.blog.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ProjectCreateDTO {

    @Schema(description = "项目标题")
    private String title;

    @Schema(description = "项目描述")
    private String description;

    @Schema(description = "项目详细描述")
    private String longDescription;

    @Schema(description = "项目图片")
    private String image;

    @Schema(description = "技术栈列表")
    private List<String> technologies;

    @Schema(description = "项目链接")
    private String url;

    @Schema(description = "源码链接")
    private String liveUrl;
}
