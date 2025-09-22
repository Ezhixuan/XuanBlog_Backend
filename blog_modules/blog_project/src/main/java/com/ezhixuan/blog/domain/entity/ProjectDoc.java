package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDoc {
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "项目 FK")
  @TableField("project_id")
  private Long projectId;

  @Schema(description = "文档标题")
  private String title;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;
}
