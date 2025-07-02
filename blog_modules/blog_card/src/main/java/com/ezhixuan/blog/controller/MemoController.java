package com.ezhixuan.blog.controller;

import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.MemoCardSubmitDTO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.entity.OperationById;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.MemoCardService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo")
class MemoController {

    @Resource
    private MemoCardService cardService;

    @Operation(summary = "删除")
    @DeleteMapping("/del/{id}")
    public BaseResponse<String> del(OperationById idOpt) {
        ThrowUtils.throwIf(Objects.isNull(idOpt) || Objects.isNull(idOpt.getId()), ErrorCode.PARAMS_ERROR);
        cardService.removeById(idOpt.getId());
        return R.success();
    }

    @Operation(summary = "新增")
    @PostMapping("/update")
    public BaseResponse<String> add(@RequestBody MemoCardSubmitDTO submitDTO) {
        ThrowUtils.throwIf(Objects.isNull(submitDTO.getBack()) || Objects.isNull(submitDTO.getFront()),
                ErrorCode.PARAMS_ERROR);
        cardService.saveOrUpdate(submitDTO.toEntity());
        return R.success();
    }
}
