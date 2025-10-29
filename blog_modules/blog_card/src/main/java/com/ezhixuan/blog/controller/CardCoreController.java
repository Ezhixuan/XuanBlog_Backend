package com.ezhixuan.blog.controller;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.dto.CardQueryDTO;
import com.ezhixuan.blog.controller.dto.CardTestDTO;
import com.ezhixuan.blog.controller.vo.CardVo;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 记忆卡片核心控制器
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Tag(name = "CardCoreController", description = "记忆卡片核心控制器")
public class CardCoreController {

    private final CardService cardService;

    @GetMapping
    @Operation(summary = "查询卡片列表")
    public PageResponse<CardVo> pageCard(CardQueryDTO queryDTO) {
        return R.list(cardService.list(queryDTO));
    }

    @PostMapping("/test")
    @Operation(summary = "记忆测试")
    public BaseResponse<Void> doTest(@RequestBody CardTestDTO testDTO) {
        cardService.doTest(testDTO);
        return R.success();
    }

    @GetMapping("/test")
    @Operation(summary = "获取测试题")
    public PageResponse<CardVo> pageTestCard(CardQueryDTO queryDTO) {
        return R.list(cardService.pageTestCardVo(queryDTO));
    }

}
