package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.entity.memo.MemoDecks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.vo.MemoDeckVO;

import java.util.List;

/**
* @author ezhixuan
* @description 针对表【memo_decks】的数据库操作Service
* @createDate 2025-05-14 11:14:26
*/
public interface MemoDecksService extends IService<MemoDecks> {

    /**
     * 获取 vo 数据
     *
     * @author Ezhixuan
     * @param list
     * @return List<MemoDeckVO>
     */
    List<MemoDeckVO> getMemoVOList(List<MemoDecks> list);
}
