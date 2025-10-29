package com.ezhixuan.blog.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 卡片测试 DTO
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class CardTestDTO {

  @Schema(description = "测试 ID")
  private Long testId;

  @Schema(description = "用时")
  private long useTime;

  @Schema(description = "是否成功")
  private boolean success;
}
