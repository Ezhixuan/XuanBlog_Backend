package com.ezhixuan.blog.domain.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("article_content")
public class ArticleContent {
  @Schema(description = "PK + FK → article.id")
  @TableId(value = "article_id", type = IdType.INPUT)
  private Long articleId;

  @Schema(description = "正文（MD/HTML）")
  private String content;

  @Schema(description = "内容引用的图片 id 数组 [int, ...]")
  @TableField("picture_ids")
  private String pictureIds;

  @Schema(description = "更新时间")
  @TableField("update_time")
  private LocalDateTime updateTime;

  public List<Long> getPictureIdList() {
      return JSON.parseArray(pictureIds, Long.class);
  }

  public void setPictureIds(List<Long> pictureIds) {
      this.pictureIds = JSON.toJSONString(pictureIds);
  }

  public void setPictureIds(String pictureIds) {
      this.pictureIds = pictureIds;
  }
}
