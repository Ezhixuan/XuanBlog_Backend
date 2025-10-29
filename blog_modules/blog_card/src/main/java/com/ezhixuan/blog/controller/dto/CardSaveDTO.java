package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.domain.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 卡片保存 dto
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class CardSaveDTO {

  @Schema(description = "回忆线索（问题）")
  private String front;

  @Schema(description = "标准答案")
  private String back;

  @Schema(description = "辅助记忆信息（图片/助记'/音频等）")
  private String context;

  @Schema(description = "父卡片 ID，用于卡片层级/模板")
  private Long parentId;

  @Schema(description = "所属卡片集 ID")
  private Long deckId;

  public Card toEntity() {
    Card card = new Card();
    card.setFront(front);
    card.setBack(back);
    card.setContext(context);
    card.setNextTime(LocalDate.now().plusDays(1));
    card.setParentId(parentId);
    card.setDeckId(deckId);
    card.setReps(0L);
    card.setLapses(0L);
    card.setEF(0.25);
    return card;
  }
}
