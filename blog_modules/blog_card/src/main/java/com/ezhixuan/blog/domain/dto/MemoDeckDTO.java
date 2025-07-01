package com.ezhixuan.blog.domain.dto;

import java.util.Date;

import com.ezhixuan.blog.domain.entity.MemoDecks;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

@Data
public class MemoDeckDTO {

    private Long id;

    private String name;

    public MemoDecks toEntity() {
        MemoDecks memoDecks = BeanUtil.copyProperties(this, MemoDecks.class);
        Date date = new Date();
        memoDecks.setCreateTime(date);
        memoDecks.setUpdateTime(date);
        return memoDecks;
    }
}
