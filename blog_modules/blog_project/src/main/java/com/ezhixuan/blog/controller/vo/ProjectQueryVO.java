package com.ezhixuan.blog.controller.vo;

import com.ezhixuan.blog.domain.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectQueryVO {

    @Schema(description = "项目id")
    private Long id;

    @Schema(description = "项目标题")
    private String title;

    @Schema(description = "项目描述")
    private String description;

    @Schema(description = "项目长描述")
    private String longDescription;

    @Schema(description = "项目图片")
    private String image;

    @Schema(description = "项目技术栈")
    private List<String> technologies;

    @Schema(description = "项目链接")
    private String url;

    @Schema(description = "项目源码链接")
    private String liveUrl;

    @Schema(description = "上架时间")
    private LocalDateTime createTime;

    @Schema(description = "stars")
    private Integer stars = 1;

    @Schema(description = "views")
    private Integer views = 1;

    @Schema(description = "是否精选")
    private boolean featured;

    @Schema(description = "是否包含文章")
    private boolean hasArticles;

    public ProjectQueryVO(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.longDescription = project.getLongDescription();
        this.image = project.getImage();
        this.url = project.getUrl();
        this.liveUrl = project.getLiveUrl();
        this.createTime = project.getCreateTime();
        this.featured = project.getFeatured();
    }
}
