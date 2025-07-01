package com.ezhixuan.blog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.MemoDecks;
import com.ezhixuan.blog.domain.vo.MemoDeckVO;
import com.ezhixuan.blog.mapper.MemoDecksMapper;
import com.ezhixuan.blog.service.MemoDecksService;

import cn.hutool.core.bean.BeanUtil;

/**
* @author ezhixuan
* @description 针对表【memo_decks】的数据库操作Service实现
* @createDate 2025-05-14 11:14:26
*/
@Service
public class MemoDecksServiceImpl extends ServiceImpl<MemoDecksMapper, MemoDecks>
    implements MemoDecksService{

    /**
     * 获取 vo 数据
     *
     * @param list
     * @return List<MemoDeckVO>
     * @author Ezhixuan
     */
    @Override
    public List<MemoDeckVO> getMemoVOList(List<MemoDecks> list) {
        return BeanUtil.copyToList(list, MemoDeckVO.class);
    }
}




