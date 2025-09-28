package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.domain.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectEditDTO extends ProjectCreateDTO{

    @Schema(description = "项目id")
    private Long id;

    @Schema(description = "首页文章 id")
    private Long indexArtId;

    public Project toEntity() {
        Project project = super.toEntity();
        project.setId(id);
        project.setIndexArtId(indexArtId);
        return project;
    }

}
