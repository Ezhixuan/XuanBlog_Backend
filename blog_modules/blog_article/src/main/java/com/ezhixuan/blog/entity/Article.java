package com.ezhixuan.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  @Schema(description = "标题")
  private String title;

  @Schema(description = "封面图 FK→picture.id")
  @TableField("cover_id")
  private Long coverId;

  @Schema(description = "摘要")
  private String summary;

  @Schema(description = "阅读数")
  @TableField("view_count")
  private Integer viewCount;

  @Schema(description = "点赞数")
  @TableField("like_count")
  private Integer likeCount;

  @Schema(description = "字数")
  @TableField("word_count")
  private Integer wordCount;

  @Schema(description = "0=草稿 1=发布")
  private Integer status;

  @Schema(description = "分类 FK→category.id")
  @TableField("category_id")
  private Long categoryId;

  @Schema(description = "项目 FK→project.id")
  @TableField("project_id")
  private Long projectId;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;

  @Schema(description = "更新时间")
  @TableField("update_time")
  private LocalDateTime updateTime;
}
