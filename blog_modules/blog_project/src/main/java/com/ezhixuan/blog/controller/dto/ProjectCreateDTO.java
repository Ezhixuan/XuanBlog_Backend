package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.domain.entity.Project;
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

    @Schema(description = "是否精选")
    private boolean featured;

    public Project toEntity() {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setLongDescription(longDescription);
        project.setImage(image);
        project.setUrl(url);
        project.setLiveUrl(liveUrl);
        project.setFeatured(featured);
        return project;
    }
}
