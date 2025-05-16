package com.ezhixuan.xuanblog_backend.controller.memo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoQueryDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.PageResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.MemoCardVO;
import com.ezhixuan.xuanblog_backend.service.MemoCardService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo")
class MemoCardCoreController {

    @Resource
    private MemoCardService memoCardService;

    @PostMapping("/operate")
    public BaseResponse<String> operate(MemoCardOperateDTO operateDTO) {
        memoCardService.operate(operateDTO);
        return R.success();
    }

    @GetMapping("/list")
    public BaseResponse<PageResponse<MemoCardVO>> list(MemoQueryDTO queryDTO) {
        IPage<MemoCardVO> memoCardVOIPage = memoCardService.getMemoPageVOList(queryDTO);
        return R.list(memoCardVOIPage);
    }
}
