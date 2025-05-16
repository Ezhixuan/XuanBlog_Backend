package com.ezhixuan.xuanblog_backend.controller.memo;

import java.util.Objects;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.OperationById;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoCardSubmitDTO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.MemoCardService;

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
    public BaseResponse<String> add(MemoCardSubmitDTO submitDTO) {
        ThrowUtils.throwIf(Objects.isNull(submitDTO.getBack()) || Objects.isNull(submitDTO.getFront()),
            ErrorCode.PARAMS_ERROR);
        cardService.saveOrUpdate(submitDTO.toEntity());
        return R.success();
    }

}
