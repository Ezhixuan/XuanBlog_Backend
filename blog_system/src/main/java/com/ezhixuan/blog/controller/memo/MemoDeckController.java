package com.ezhixuan.blog.controller.memo;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.MemoDeckDTO;
import com.ezhixuan.blog.domain.entity.memo.MemoDecks;
import com.ezhixuan.blog.domain.vo.MemoDeckVO;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.entity.OperationById;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.service.MemoDecksService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo/deck")
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
