package com.ezhixuan.xuanblog_backend.domain.constant;

/**
 * 记忆卡片复习步长
 */
public interface MemoConstant {

    // ========== SM-2算法兼容常量 ==========
    Long INIT_REVIEW_INTERVAL = 20L;

    Long SECOND_REVIEW_INTERVAL = 60L;

    Long THIRD_REVIEW_INTERVAL = 180L;

    Long FOURTH_REVIEW_INTERVAL = 540L;

    Long MAX_REVIEW_INTERVAL = 43200L;

    double DEFAULT_EASE_FACTOR = 2.5;

    double MIN_EASE_FACTOR = 1.3;

    // ========== SM-17算法增强常量 ==========
    
    /**
     * 遗忘指数 - 默认10%，表示期望的遗忘率
     */
    double DEFAULT_FORGETTING_INDEX = 0.1;
    
    /**
     * 默认检索成功率阈值 - 90%，对应10%的遗忘指数
     */
    double DEFAULT_RETRIEVABILITY_THRESHOLD = 0.9;
    
    /**
     * 记忆衰减常数 - 用于指数衰减计算
     */
    double MEMORY_DECAY_CONSTANT = 0.05;
    
    /**
     * 稳定性增长函数的基础参数
     */
    double STABILITY_INCREASE_BASE = 1.2;
    
    /**
     * 稳定性增长的最大倍数
     */
    double MAX_STABILITY_INCREASE = 15.0;
    
    /**
     * 稳定性增长的最小倍数
     */
    double MIN_STABILITY_INCREASE = 1.0;
    
    /**
     * 启动稳定性的最大值（天数）
     */
    int MAX_STARTUP_STABILITY = 90;
    
    /**
     * 初始稳定性的默认值（天数）- 基于首次遗忘曲线
     */
    double DEFAULT_INITIAL_STABILITY = 4.0;
    
    /**
     * 检索能力衰减常数 - 用于计算R值
     */
    double RETRIEVABILITY_DECAY_CONSTANT = 1.0;
    
    /**
     * 难度计算的权重系数
     */
    double DIFFICULTY_WEIGHT = 5.0;
    
    /**
     * 稳定性对间隔影响的衰减指数
     */
    double STABILITY_DECAY_POWER = -0.17;
    
    /**
     * 检索能力对稳定性增长影响的最优值
     */
    double OPTIMAL_RETRIEVABILITY_FOR_STABILITY = 0.9;
}
