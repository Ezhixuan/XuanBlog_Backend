package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectQueryDTO extends PageRequest {

  @Schema(description = "是否精选")
  private Boolean featured;

  @Schema(description = "技术栈")
  private Long technologyId;

  @Schema(description = "项目Id")
  private List<Long> projectIds = new ArrayList<>();
}
