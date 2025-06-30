package com.ezhixuan.blog.domain.entity.memo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 记忆卡片实体
 *
 * 支持SM-17算法的三组件记忆模型：
 * - Difficulty: 项目难度
 * - Stability: 记忆稳定性
 * - Retrievability: 检索能力
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
     * 重复间隔（分钟）
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
     * 复习情况(质量分数 0-5)
     */
    @TableField(value = "quality")
    private Long quality;

    /**
     * 用于动态调整复习间隔（SM-2算法兼容）
     */
    @TableField(value = "ease_factor")
    private Double easeFactor;

    // ========== SM-17算法新增字段 ==========

    /**
     * 项目难度 (0-1范围，0最容易，1最困难)
     * SM-17算法的D组件
     */
    @TableField(value = "difficulty")
    private Double difficulty;

    /**
     * 记忆稳定性（天数）
     * SM-17算法的S组件 - 表示在90%检索成功率下可维持的时间
     */
    @TableField(value = "stability")
    private Double stability;

    /**
     * 当前检索能力 (0-1范围)
     * SM-17算法的R组件 - 表示当前时刻成功回忆的概率
     */
    @TableField(value = "retrievability")
    private Double retrievability;

    /**
     * 上次复习时间
     * 用于计算时间间隔和检索能力衰减
     */
    @TableField(value = "last_review_time")
    private LocalDateTime lastReviewTime;

    /**
     * 算法版本标识
     * SM2: 使用传统SM-2算法
     * SM17: 使用SM-17算法
     */
    @TableField(value = "algorithm_version")
    private String algorithmVersion;

    /**
     * 记忆失败次数
     * 用于失败后的间隔计算
     */
    @TableField(value = "lapse_count")
    private Long lapseCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
