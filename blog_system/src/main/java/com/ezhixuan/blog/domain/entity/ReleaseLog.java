package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReleaseLog {
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "版本号")
  private String version;

  @Schema(description = "一句话标题")
  private String title;

  @Schema(description = "详情")
  private String content;

  @Schema(description = "1-功能 2-修复 3-优化")
  private Integer type;

  @Schema(description = "1-普通 2-重要 3-紧急")
  private Integer level;

  @Schema(description = "0-草稿 1-发布")
  private Integer status;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;
}
