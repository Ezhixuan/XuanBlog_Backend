package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Deck {
  @Schema(description = "主键，自增")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "卡片集名称")
  private String name;

  @Schema(description = "是否私有")
  @TableField("private")
  private boolean privateDeck;
}
