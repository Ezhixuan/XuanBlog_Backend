package com.ezhixuan.xuanblog_backend.domain.dto;

import java.time.LocalDateTime;

public class MemoPageDTO {

    /**
     * 主键 id
     */
    private Long id;

    /**
     * 卡片集
     */
    private Long deckId;

    /**
     * 卡片集名称
     */
    private String deckName;

    /**
     * 问题
     */
    private String front;

    /**
     * 答案
     */
    private String back;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 重复间隔
     */
    private Long reviewInterval;

    /**
     * 下次复习时间
     */
    private LocalDateTime nextReviewDate;

    /**
     * 已经被复习的次数
     */
    private Long repetitions;

    /**
     * 复习情况
     */
    private Long quality;

    /**
     * 用于动态调整复习间隔
     */
    private Double easeFactor;
}
