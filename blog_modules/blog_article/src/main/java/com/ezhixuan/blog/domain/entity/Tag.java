package com.ezhixuan.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tag")
public class Tag {

  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "标签名称")
  private String name;

  @Schema(description = "创建时间")
  @TableField("create_time")
  private LocalDateTime createTime;
}
