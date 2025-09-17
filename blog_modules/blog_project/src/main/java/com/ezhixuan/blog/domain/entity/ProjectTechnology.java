package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProjectTechnology {

  @Schema(description = "项目 id")
  @TableId(value = "project_id", type = IdType.INPUT)
  private Long projectId;

  @Schema(description = "技术栈 id")
  @TableField("technology_id")
  private Long technologyId;
}
