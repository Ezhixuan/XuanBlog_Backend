package com.ezhixuan.xuanblog_backend.controller.memo;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.OperationById;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoDeckDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoDecks;
import com.ezhixuan.xuanblog_backend.domain.vo.MemoDeckVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.MemoDecksService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/deck")
class MemoDeckController {

    @Resource
    private MemoDecksService decksService;

    @Operation(summary = "列表")
    @GetMapping("/list")
    public BaseResponse<PageResponse<MemoDeckVO>> listAll() {
        List<MemoDeckVO> vos = decksService
            .getMemoVOList(decksService.list(Wrappers.<MemoDecks>lambdaQuery().orderByDesc(MemoDecks::getUpdateTime)));
        return R.list(vos);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/del/{id}")
    public BaseResponse<String> del(OperationById idOpt) {
        ThrowUtils.throwIf(Objects.isNull(idOpt) || Objects.isNull(idOpt.getId()), ErrorCode.PARAMS_ERROR);
        decksService.removeById(idOpt.getId());
        return R.success();
    }

    @Operation(summary = "新增")
    @PostMapping("/update")
    public BaseResponse<String> add(@RequestBody MemoDeckDTO memoDeckDTO) {
        ThrowUtils.throwIf(Objects.isNull(memoDeckDTO) || Objects.isNull(memoDeckDTO.getName()),
            ErrorCode.PARAMS_ERROR);
        decksService.saveOrUpdate(memoDeckDTO.toEntity());
        return R.success();
    }

}
