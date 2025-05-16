package com.ezhixuan.xuanblog_backend.domain.entity.memo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 *
 * @TableName memo_card
 */
@TableName(value ="memo_card")
@Data
public class MemoCard implements Serializable {
    /**
     * 主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 卡片集
     */
    @TableField(value = "deck_id")
    private Long deckId;

    /**
     * 问题
     */
    @TableField(value = "front")
    private String front;

    /**
     * 答案
     */
    @TableField(value = "back")
    private String back;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 重复间隔
     */
    @TableField(value = "review_interval")
    private Long reviewInterval;

    /**
     * 下次复习时间
     */
    @TableField(value = "next_review_date")
    private LocalDateTime nextReviewDate;

    /**
     * 已经被复习的次数
     */
    @TableField(value = "repetitions")
    private Long repetitions;

    /**
     * 复习情况
     */
    @TableField(value = "quality")
    private Long quality;

    /**
     * 用于动态调整复习间隔
     */
    @TableField(value = "ease_factor")
    private Double easeFactor;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
