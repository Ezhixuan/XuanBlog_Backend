package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.entity.Deck;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

/**
 * 卡片集控制器
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@RestController
@RequestMapping("deck")
@RequiredArgsConstructor
@Tag(name = "DeckController", description = "卡片集控制器")
public class DeckController {

  private final DeckService deckService;

  @PostMapping
  @Operation(summary = "新增卡片集")
  public BaseResponse<Void> addDeck(@RequestBody Deck deck) {
    deckService.save(deck);
    return R.success();
  }

  @PutMapping
  @Operation(summary = "修改卡片集")
  public BaseResponse<Void> updateDeck(@RequestBody Deck deck) {
    if (isNull(deck) || isNull(deck.getId())) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    deckService.updateById(deck);
    return R.success();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "删除卡片集")
  public BaseResponse<Void> deleteDeck(@PathVariable Long id) {
    deckService.removeById(id);
    return R.success();
  }

  @GetMapping("/{id}")
  @Operation(summary = "获取卡片集")
  public BaseResponse<Deck> getDeck(@PathVariable Long id) {
    return R.success(deckService.getById(id));
  }

  @GetMapping
  @Operation(summary = "获取所有卡片集")
  public PageResponse<Deck> getAllDeck() {
    return R.list(deckService .listByUser());
  }
}
