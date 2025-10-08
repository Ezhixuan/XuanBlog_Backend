package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Card {
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  @Schema(description = "回忆线索（问题）")
  private String front;

  @Schema(description = "标准答案")
  private String back;

  @Schema(description = "辅助记忆信息（图片/助记/音频等）")
  private String context;

  @Schema(description = "简易度因子（SM-2 算法）")
  private Double EF;

  @Schema(description = "下次复习日期")
  @TableField("next_time")
  private LocalDate nextTime;

  @Schema(description = "父卡片 ID，用于卡片层级/模板")
  @TableField("parent_id")
  private Long parentId;

  @Schema(description = "所属卡片集 ID")
  @TableField("desk_id")
  private Long deskId;

  @Schema(description = "累计复习次数")
  private Long reps;

  @Schema(description = "累计遗忘次数")
  private Long lapses;
}
