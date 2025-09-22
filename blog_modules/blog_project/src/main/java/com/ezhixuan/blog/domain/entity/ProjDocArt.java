package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProjDocArt {
  @Schema(description = "文章 FK")
  @TableId(value = "article_id", type = IdType.INPUT)
  private Long articleId;

  @Schema(description = "文档 FK")
  @TableField("doc_id")
  private Long docId;

  @Schema(description = "同分类内排序")
  @TableField("sort_order")
  private Integer sortOrder;
}
