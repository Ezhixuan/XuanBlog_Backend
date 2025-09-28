package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Project {

  @Schema(description = "主键 id")
  private Long id;

  @Schema(description = "首页文章 id")
  @TableField("index_art_id")
  private Long indexArtId;

  @Schema(description = "项目标题")
  private String title;

  @Schema(description = "项目描述")
  private String description;

  @Schema(description = "项目详细描述")
  @TableField("long_description")
  private String longDescription;

  @Schema(description = "项目图片")
  private String image;

  @Schema(description = "项目链接")
  private String url;

  @Schema(description = "源码链接")
  @TableField("live_url")
  private String liveUrl;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;

  @Schema(description = "是否精选 0 否 1 是")
  private Boolean featured;
}
