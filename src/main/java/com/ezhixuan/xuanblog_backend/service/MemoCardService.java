package com.ezhixuan.xuanblog_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.xuanblog_backend.domain.vo.MemoCardVO;

/**
* @author ezhixuan
* @description 针对表【memo_card】的数据库操作Service
* @createDate 2025-05-14 11:13:15
*/
public interface MemoCardService extends IService<MemoCard> {

    /**
     * 对卡片进行操作 会根据 type 以及 useTime 对考核分进行计算 以变更下一次的记忆时间
     *
     * @author Ezhixuan
     * @param operateDTO 操作参数
     */
    void operate(MemoCardOperateDTO operateDTO);

    /**
     * 查询vo
     * @author Ezhixuan
     * @param queryDTO 查询 dto
     * @return 已筛选的卡片集信息
     */
    IPage<MemoCardVO> getMemoPageVOList(MemoQueryDTO queryDTO);
}
