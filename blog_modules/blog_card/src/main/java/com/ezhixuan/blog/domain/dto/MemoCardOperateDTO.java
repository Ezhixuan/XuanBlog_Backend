package com.ezhixuan.blog.domain.dto;

import lombok.Data;

@Data
public class MemoCardOperateDTO {
    private Long id;
    private long useTime;

    /**
     * 回答感觉 0 easy 1 mid 2 hard
     */
    private int type;
}
