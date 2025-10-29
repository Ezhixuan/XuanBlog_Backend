package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 卡牌查询 DTO
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CardQueryDTO extends PageRequest {

  @Schema(description = "卡片 id")
  private Long id;

  @Schema(description = "卡片集 id")
  private Long deckId;

  @Schema(description = "时间")
  private LocalDateTime time;
}
