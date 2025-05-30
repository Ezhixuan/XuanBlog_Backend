package com.ezhixuan.xuanblog_backend.utils;

import static com.ezhixuan.xuanblog_backend.domain.constant.MemoConstant.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * SM-17 算法工具类
 * 
 * <p>实现基于三组件记忆模型(DSR)的间隔重复算法：
 * <ul>
 * <li>D (Difficulty): 项目难度 (0-1范围)</li>
 * <li>S (Stability): 记忆稳定性 (天数)</li>
 * <li>R (Retrievability): 检索能力 (0-1范围，表示回忆成功的概率)</li>
 * </ul>
 * 
 * <p>相比SM-2算法的主要改进：
 * <ul>
 * <li>增加了检索能力(R)维度，能更好地处理非最优时间间隔的复习</li>
 * <li>使用更精确的稳定性增长函数</li>
 * <li>考虑了时间间隔对记忆巩固的影响</li>
 * <li>更科学的难度评估机制</li>
 * </ul>
 * 
 * @author ezhixuan
 */
@UtilityClass
@Slf4j
public class Sm17AlgorithmUtil {

    /**
     * 计算理论检索能力(Retrievability)
     * 
     * 使用指数衰减模型：R = exp(-k * t / S)
     * 
     * @param stability 当前记忆稳定性(天数)
     * @param intervalDays 距离上次复习的天数
     * @return 检索能力值 (0-1范围)
     */
    public static double calculateRetrievability(double stability, long intervalDays) {
        if (stability <= 0) {
            log.warn("稳定性值异常: {}", stability);
            return 0.0;
        }
        if (intervalDays < 0) {
            log.warn("间隔天数异常: {}", intervalDays);
            return 1.0;
        }
        
        // R = exp(-k * t / S)
        double retrievability = Math.exp(-RETRIEVABILITY_DECAY_CONSTANT * intervalDays / stability);
        return Math.max(0.0, Math.min(1.0, retrievability));
    }

    /**
     * 计算稳定性增长因子 (SInc) - SM-17核心公式
     * 
     * 基于三维函数：SInc[D, S, R]
     * 考虑难度、当前稳定性和检索能力的综合影响
     * 
     * @param difficulty 项目难度 (0-1，0最容易，1最困难)
     * @param currentStability 当前稳定性(天数)
     * @param retrievability 检索能力 (0-1)
     * @return 稳定性增长因子
     */
    public static double calculateStabilityIncrease(double difficulty, double currentStability, double retrievability) {
        // 参数验证
        difficulty = Math.max(0.0, Math.min(1.0, difficulty));
        retrievability = Math.max(0.0, Math.min(1.0, retrievability));
        currentStability = Math.max(0.1, currentStability);
        
        // 基础稳定性增长：考虑难度的影响
        // 简单的项目(difficulty接近0)有更高的增长潜力
        double difficultyFactor = DIFFICULTY_WEIGHT * (1 - difficulty) + 1;
        
        // 检索能力影响：接近最优检索率时增长最大
        // 使用倒U型曲线，在90%检索率附近达到峰值
        double retrievabilityFactor = calculateRetrievabilityFactor(retrievability);
        
        // 稳定性衰减影响：随着稳定性增加，增长幅度递减
        // 使用幂函数模拟边际递减效应
        double stabilityDecayFactor = Math.pow(currentStability, STABILITY_DECAY_POWER);
        
        // 综合计算稳定性增长
        double stabilityIncrease = STABILITY_INCREASE_BASE * difficultyFactor * retrievabilityFactor * stabilityDecayFactor;
        
        // 应用上下限约束
        stabilityIncrease = Math.max(MIN_STABILITY_INCREASE, Math.min(MAX_STABILITY_INCREASE, stabilityIncrease));
        
        log.debug("稳定性增长计算 - 难度:{}, 稳定性:{}, 检索能力:{} => 增长因子:{}", 
                 difficulty, currentStability, retrievability, stabilityIncrease);
        
        return stabilityIncrease;
    }

    /**
     * 计算检索能力对稳定性增长的影响因子
     * 
     * 使用改进的倒U型函数，在最优检索率(90%)附近达到峰值
     * 
     * @param retrievability 检索能力
     * @return 影响因子
     */
    private static double calculateRetrievabilityFactor(double retrievability) {
        // 距离最优检索率的偏差
        double deviation = Math.abs(retrievability - OPTIMAL_RETRIEVABILITY_FOR_STABILITY);
        
        // 使用高斯函数的变形，在最优点附近有最大值
        double factor = Math.exp(-4 * deviation * deviation);
        
        // 确保最小值为0.1，避免过度惩罚
        return Math.max(0.1, factor);
    }

    /**
     * 根据质量分数更新记忆稳定性
     * 
     * @param currentStability 当前稳定性
     * @param difficulty 项目难度
     * @param retrievability 检索能力
     * @param quality 质量分数 (0-5)
     * @return 新的稳定性值
     */
    public static double updateStability(double currentStability, double difficulty, double retrievability, long quality) {
        if (quality < 2) {
            // 回忆失败，稳定性重置为初始值
            log.debug("回忆失败，稳定性重置: {} -> {}", currentStability, DEFAULT_INITIAL_STABILITY);
            return DEFAULT_INITIAL_STABILITY;
        }
        
        // 回忆成功，计算新的稳定性
        double stabilityIncrease = calculateStabilityIncrease(difficulty, currentStability, retrievability);
        double newStability = currentStability * stabilityIncrease;
        
        log.debug("稳定性更新: {} * {} = {}", currentStability, stabilityIncrease, newStability);
        return newStability;
    }

    /**
     * 计算下次复习的最优间隔
     * 
     * 基于稳定性和期望的遗忘指数
     * 
     * @param stability 记忆稳定性
     * @param forgettingIndex 期望的遗忘指数 (默认0.1，即10%)
     * @return 最优间隔（分钟）
     */
    public static long calculateOptimalInterval(double stability, double forgettingIndex) {
        // 根据遗忘指数计算对应的检索能力阈值
        double targetRetrievability = 1.0 - forgettingIndex;
        
        // 通过检索能力公式反推最优间隔：t = -S * ln(R) / k
        double optimalDays = -stability * Math.log(targetRetrievability) / RETRIEVABILITY_DECAY_CONSTANT;
        
        // 转换为分钟并确保合理范围
        long intervalMinutes = Math.round(optimalDays * 24 * 60);
        intervalMinutes = Math.max(INIT_REVIEW_INTERVAL, Math.min(MAX_REVIEW_INTERVAL, intervalMinutes));
        
        log.debug("最优间隔计算 - 稳定性:{} 天, 遗忘指数:{} => 间隔:{} 分钟", 
                 stability, forgettingIndex, intervalMinutes);
        
        return intervalMinutes;
    }

    /**
     * 基于历史表现计算项目难度
     * 
     * 使用简化的难度估算方法，结合最近的表现和历史平均
     * 
     * @param currentDifficulty 当前难度估计
     * @param quality 本次质量分数
     * @param expectedRetrievability 期望的检索能力
     * @param actualRetrievability 实际的检索能力(基于质量分数推算)
     * @return 更新后的难度值
     */
    public static double updateDifficulty(double currentDifficulty, long quality, 
                                         double expectedRetrievability, double actualRetrievability) {
        // 计算表现偏差
        double performanceDeviation = expectedRetrievability - actualRetrievability;
        
        // 根据偏差调整难度：表现差于预期则增加难度，反之减少难度
        double difficultyAdjustment = performanceDeviation * 0.1; // 10%的调整幅度
        
        // 质量分数的直接影响：低质量分数增加难度
        double qualityImpact = (3.0 - quality) * 0.05; // 质量3为中性点
        
        double newDifficulty = currentDifficulty + difficultyAdjustment + qualityImpact;
        
        // 应用0-1范围约束
        newDifficulty = Math.max(0.0, Math.min(1.0, newDifficulty));
        
        log.debug("难度更新: {} + {} + {} = {}", 
                 currentDifficulty, difficultyAdjustment, qualityImpact, newDifficulty);
        
        return newDifficulty;
    }

    /**
     * 根据质量分数估算实际检索能力
     * 
     * @param quality 质量分数 (0-5)
     * @return 估算的检索能力
     */
    public static double estimateRetrievabilityFromQuality(long quality) {
        // 质量分数到检索能力的映射
        return switch ((int) quality) {
            case 0, 1 -> 0.0;  // 失败
            case 2 -> 0.6;     // 勉强通过
            case 3 -> 0.8;     // 一般
            case 4 -> 0.95;    // 良好
            case 5 -> 1.0;     // 完美
            default -> 0.5;    // 默认值
        };
    }

    /**
     * 计算两个时间点之间的天数差
     * 
     * @param lastReview 上次复习时间
     * @param currentTime 当前时间
     * @return 间隔天数
     */
    public static long calculateDaysBetween(LocalDateTime lastReview, LocalDateTime currentTime) {
        if (lastReview == null || currentTime == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(lastReview.toLocalDate(), currentTime.toLocalDate());
    }

    /**
     * 计算初始稳定性
     * 
     * 基于首次复习的表现估算初始稳定性
     * 
     * @param quality 首次质量分数
     * @return 初始稳定性(天数)
     */
    public static double calculateInitialStability(long quality) {
        // 基于质量分数调整初始稳定性
        double adjustment = (quality - 3.0) * 0.5; // 质量3为基准点
        double initialStability = DEFAULT_INITIAL_STABILITY + adjustment;
        
        // 确保在合理范围内
        return Math.max(1.0, Math.min(MAX_STARTUP_STABILITY, initialStability));
    }
} 