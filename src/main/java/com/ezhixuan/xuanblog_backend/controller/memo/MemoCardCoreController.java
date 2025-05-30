package com.ezhixuan.xuanblog_backend.controller.memo;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.MemoCardVO;
import com.ezhixuan.xuanblog_backend.service.MemoCardService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo")
class MemoCardCoreController {

    @Resource
    private MemoCardService memoCardService;

    @PostMapping("/operate")
    public BaseResponse<String> operate(@RequestBody MemoCardOperateDTO operateDTO) {
        memoCardService.operate(operateDTO);
        return R.success();
    }

    @GetMapping("/list")
    public BaseResponse<PageResponse<MemoCardVO>> list(MemoQueryDTO queryDTO) {
        IPage<MemoCardVO> memoCardVOIPage = memoCardService.getMemoPageVOList(queryDTO);
        return R.list(memoCardVOIPage);
    }

    @PostMapping("/test")
    public BaseResponse<List<MemoCardVO>> test(Long deckId) {
        List<MemoCardVO> memoCardVOList = memoCardService.test(deckId);
        return R.success(memoCardVOList);
    }
}
