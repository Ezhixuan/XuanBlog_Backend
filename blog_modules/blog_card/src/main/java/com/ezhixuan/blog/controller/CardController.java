package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.dto.CardSaveDTO;
import com.ezhixuan.blog.controller.dto.CardUpdateDTO;
import com.ezhixuan.blog.domain.entity.Card;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

/**
 * 记忆卡片控制器
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@RestController
@RequestMapping("card")
@RequiredArgsConstructor
@Tag(name = "CardController", description = "记忆卡片控制器")
public class CardController {

  private final CardService cardService;

  @PostMapping
  @Operation(summary = "新增记忆卡片")
  public BaseResponse<Void> addCard(@RequestBody CardSaveDTO saveDTO) {
    if (isNull(saveDTO)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    cardService.save(saveDTO.toEntity());
    return R.success();
  }

  @PutMapping
  @Operation(summary = "修改记忆卡片")
  public BaseResponse<Void> updateCard(@RequestBody CardUpdateDTO updateDTO) {
    if (isNull(updateDTO) || isNull(updateDTO.getId())) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    cardService.updateById(updateDTO.toEntity());
    return R.success();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "删除记忆卡片")
  public BaseResponse<Void> deleteCard(@PathVariable Long id) {
    cardService.removeById(id);
    return R.success();
  }

  @GetMapping("/{id}")
  @Operation(summary = "获取记忆卡片")
  public BaseResponse<Card> getCard(@PathVariable Long id) {
    return R.success(cardService.getById(id));
  }
}
