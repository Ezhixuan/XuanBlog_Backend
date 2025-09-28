package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArticleThumb {
  @Schema(description = "主键 id")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "用户 id")
  @TableField("user_id")
  private Long userId;

  @Schema(description = "文章 id")
  @TableField("article_id")
  private Long articleId;
}
