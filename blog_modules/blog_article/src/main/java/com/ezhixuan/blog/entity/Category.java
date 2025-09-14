package com.ezhixuan.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category {
  @Schema(description = "主键")
  private Long id;

  @Schema(description = "分类名称")
  private String name;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;
}
