package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.domain.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 卡片修改 dto
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CardUpdateDTO extends CardSaveDTO {

  @Schema(description = "卡片 ID")
  private Long id;

  @Override
  public Card toEntity() {
    Card card = super.toEntity();
    card.setId(id);
    return card;
  }
}
