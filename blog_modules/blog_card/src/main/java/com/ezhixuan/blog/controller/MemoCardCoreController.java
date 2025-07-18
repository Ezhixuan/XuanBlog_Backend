package com.ezhixuan.blog.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.blog.domain.dto.MemoCardSubmitDTO;
import com.ezhixuan.blog.domain.dto.MemoQueryDTO;
import com.ezhixuan.blog.domain.vo.MemoCardVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.entity.OperationById;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.MemoCardService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo/card")
class MemoCardCoreController {

    @Resource
    private MemoCardService memoCardService;

    @Operation(summary = "卡片测试操作")
    @PostMapping("/operate")
    public BaseResponse<String> operate(@RequestBody MemoCardOperateDTO operateDTO) {
        memoCardService.operate(operateDTO);
        return R.success();
    }

    @Operation(summary = "卡片列表")
    @GetMapping("/list")
    public BaseResponse<PageResponse<MemoCardVO>> list(MemoQueryDTO queryDTO) {
        IPage<MemoCardVO> memoCardVOIPage = memoCardService.getMemoPageVOList(queryDTO);
        return R.list(memoCardVOIPage);
    }

    @Operation(summary = "卡片测试")
    @GetMapping("/test")
    public BaseResponse<List<MemoCardVO>> test(@RequestParam(required = false) Long deckId) {
        List<MemoCardVO> memoCardVOList = memoCardService.test(deckId);
        return R.success(memoCardVOList);
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public BaseResponse<String> del(@RequestBody OperationById idOpt) {
        ThrowUtils.throwIf(Objects.isNull(idOpt) || Objects.isNull(idOpt.getId()), ErrorCode.PARAMS_ERROR);
        memoCardService.removeById(idOpt.getId());
        return R.success();
    }

    @Operation(summary = "新增")
    @PostMapping("submit")
    public BaseResponse<String> add(@RequestBody MemoCardSubmitDTO submitDTO) {
        ThrowUtils.throwIf(Objects.isNull(submitDTO.getBack()) || Objects.isNull(submitDTO.getFront()), ErrorCode.PARAMS_ERROR);
        memoCardService.saveOrUpdate(submitDTO.toEntity());
        return R.success();
    }
}
