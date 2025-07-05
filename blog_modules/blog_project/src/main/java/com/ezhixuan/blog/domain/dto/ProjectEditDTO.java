package com.ezhixuan.blog.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectEditDTO extends ProjectCreateDTO{

    @Schema(description = "项目id")
    private Long id;
}
