package com.ezhixuan.blog.controller.vo;

import com.ezhixuan.blog.domain.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 卡片查询 Vo
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class CardVo {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "回忆线索（问题）")
  private String front;

  @Schema(description = "标准答案")
  private String back;

  @Schema(description = "辅助记忆信息（图片/助记'/音频等）")
  private String context;

  @Schema(description = "下次复习日期")
  private LocalDate nextTime;

  @Schema(description = "父卡片 ID，用于卡片层级/模板")
  private Long parentId;

  @Schema(description = "父卡片的front")
  private String parentFront;

  @Schema(description = "所属卡片集 ID")
  private Long deckId;

  @Schema(description = "所属卡片集名称")
  private String deckName;

  @Schema(description = "累计复习次数")
  private Long reps;

  public static CardVo fromEntity(Card card) {
    if (card == null) {
      return null;
    }
    CardVo cardVo = new CardVo();
    cardVo.setId(card.getId());
    cardVo.setFront(card.getFront());
    cardVo.setBack(card.getBack());
    cardVo.setContext(card.getContext());
    cardVo.setNextTime(card.getNextTime());
    cardVo.setParentId(card.getParentId());
    cardVo.setDeckId(card.getDeckId());
    cardVo.setReps(card.getReps());
    return cardVo;
  }
}
