package com.ezhixuan.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("article_tag")
public class ArticleTag {
  @Schema(description = "文章 FK")
  @TableId(value = "article_id", type = IdType.INPUT)
  private Long articleId;

  @Schema(description = "标签 FK")
  @TableField("tag_id")
  private Long tagId;
}
