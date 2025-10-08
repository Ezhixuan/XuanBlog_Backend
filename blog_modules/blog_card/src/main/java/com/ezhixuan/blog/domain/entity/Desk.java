package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Desk {
  @Schema(description = "主键，自增")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "卡片集名称")
  private String name;
}
