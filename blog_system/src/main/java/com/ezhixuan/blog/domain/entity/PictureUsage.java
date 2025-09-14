package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PictureUsage {
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "1-文章内容 2-文章封面 3-博客头像")
  @TableField("entity_type")
  private Integer entityType;

  @Schema(description = "业务对象 ID（文章 ID 或用户 ID）")
  @TableField("entity_id")
  private Long entityId;

  @Schema(description = "图片资源 ID → picture.id")
  @TableField("picture_id")
  private Long pictureId;

  @Schema(description = "首次引用时间")
  @TableField("create_time")
  private LocalDateTime createTime;

  @Schema(description = "更新时间")
  @TableField("update_time")
  private LocalDateTime updateTime;
}
