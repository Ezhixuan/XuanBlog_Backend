package com.ezhixuan.blog.utils;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 简化间隔重复算法
 * 基于SM-17核心思想的大幅简化版本
 * 使用固定的间隔序列和简化的三组件模型
 * 
 * 算法特点：
 * 1. 保留难度(D)、稳定性(S)、检索能力(R)三组件概念
 * 2. 使用固定间隔序列约束，避免过度复杂的计算
 * 3. 简化参数配置，易于理解和维护
 * 4. 优化边界条件处理
 */
@UtilityClass
@Slf4j
public class SimpleIntervalAlgorithm {
    
    // 固定间隔序列（天数）- 基于艾宾浩斯遗忘曲线
    private static final int[] INTERVAL_DAYS = {1, 3, 7, 14, 30, 60, 120};
    
    // 算法参数
    private static final double INITIAL_STABILITY = 1.0;      // 初始稳定性（天）
    private static final double INITIAL_DIFFICULTY = 0.5;     // 初始难度
    private static final double MIN_DIFFICULTY = 0.1;         // 最小难度
    private static final double MAX_DIFFICULTY = 0.9;         // 最大难度
    private static final double DIFFICULTY_UPDATE_RATE = 0.05; // 难度更新率（降低调整幅度）
    private static final double MIN_RETRIEVABILITY = 0.1;     // 最小检索能力
    private static final double MAX_RETRIEVABILITY = 1.0;     // 最大检索能力
    private static final double MIN_INTERVAL_DAYS = 1.0;      // 最小间隔（天）
    private static final double MAX_INTERVAL_DAYS = 365.0;    // 最大间隔（天）
    
    /**
     * 计算下次复习间隔
     * 
     * @param stability 当前稳定性（天）
     * @param difficulty 当前难度（0-1）
     * @param quality 质量分数（0-3）
     * @param reviewCount 复习次数
     * @return 下次间隔（天）
     */
    public static int calculateNextInterval(double stability, double difficulty, int quality, int reviewCount) {
        // 基础间隔基于稳定性
        double baseInterval = stability;
        
        // 难度调整：难度越高，间隔越短
        double difficultyFactor = 1.0 + (0.5 - difficulty) * 0.5; // 难度0.5为中性，调整幅度减小
        
        // 质量调整：质量越高，间隔越长
        double qualityFactor = 0.9 + (quality * 0.15); // 质量0-3映射到0.9-1.35，更加保守
        
        // 复习次数调整：随着复习次数增加，间隔增长更加谨慎
        int sequenceIndex = Math.min(reviewCount, INTERVAL_DAYS.length - 1);
        double experienceFactor = 1.0 + (sequenceIndex * 0.1); // 经验因子，逐渐增加
        
        // 计算新间隔
        double newInterval = baseInterval * difficultyFactor * qualityFactor * experienceFactor;
        
        // 应用固定间隔序列约束，避免间隔增长过快
        int standardInterval = INTERVAL_DAYS[sequenceIndex];
        double maxInterval = standardInterval * 1.5; // 允许最大1.5倍于标准间隔，更加保守
        
        // 确保在合理范围内
        newInterval = Math.max(MIN_INTERVAL_DAYS, Math.min(newInterval, maxInterval));
        newInterval = Math.min(newInterval, MAX_INTERVAL_DAYS);
        
        log.debug("间隔计算 - 稳定性:{:.1f}, 难度:{:.2f}, 质量:{}, 次数:{} => 间隔:{:.1f}天", 
                 stability, difficulty, quality, reviewCount, newInterval);
        
        return (int) Math.round(newInterval);
    }
    
    /**
     * 更新稳定性
     * 
     * @param currentStability 当前稳定性
     * @param difficulty 项目难度
     * @param quality 质量分数
     * @return 新稳定性
     */
    public static double updateStability(double currentStability, double difficulty, int quality) {
        if (quality < 2) {
            // 回忆失败，重置为初始稳定性
            log.debug("回忆失败，稳定性重置: {} -> {}", currentStability, INITIAL_STABILITY);
            return INITIAL_STABILITY;
        }
        
        // 计算稳定性增长因子 - 更加保守的增长
        double baseGrowth = 1.2; // 基础增长20%
        double difficultyBonus = (1.0 - difficulty) * 0.3; // 难度奖励，最高30%
        double qualityBonus = quality * 0.1; // 质量奖励，最高30%
        
        double growthFactor = baseGrowth + difficultyBonus + qualityBonus;
        growthFactor = Math.max(1.1, Math.min(1.8, growthFactor)); // 限制增长范围1.1-1.8，更加保守
        
        double newStability = currentStability * growthFactor;
        
        log.debug("稳定性更新: {} * {} = {}", currentStability, growthFactor, newStability);
        return newStability;
    }
    
    /**
     * 更新难度
     * 
     * @param currentDifficulty 当前难度
     * @param quality 质量分数
     * @return 新难度
     */
    public static double updateDifficulty(double currentDifficulty, int quality) {
        // 基于质量分数调整难度 - 更加温和的调整
        double qualityCenter = 1.5; // 质量分数中心点（0-3范围）
        double difficultyAdjustment = DIFFICULTY_UPDATE_RATE * (qualityCenter - quality);
        
        // 根据当前难度调整更新幅度：难度越高，调整越保守
        double difficultyAttenuation = 1.0 - (currentDifficulty * 0.3); // 难度衰减因子
        difficultyAdjustment *= difficultyAttenuation;
        
        double newDifficulty = currentDifficulty + difficultyAdjustment;
        
        // 应用边界约束
        return Math.max(MIN_DIFFICULTY, Math.min(MAX_DIFFICULTY, newDifficulty));
    }
    
    /**
     * 计算检索能力
     * 
     * @param stability 稳定性（天）
     * @param intervalDays 间隔天数
     * @return 检索能力（0-1）
     */
    public static double calculateRetrievability(double stability, long intervalDays) {
        if (stability <= 0 || intervalDays < 0) {
            return intervalDays < 0 ? MAX_RETRIEVABILITY : MIN_RETRIEVABILITY;
        }
        
        // 简化的指数衰减：R = exp(-t/S)
        double retrievability = Math.exp(-intervalDays / stability);
        
        // 应用边界约束
        return Math.max(MIN_RETRIEVABILITY, Math.min(MAX_RETRIEVABILITY, retrievability));
    }
    
    /**
     * 估算初始参数
     * 
     * @param quality 首次复习质量
     * @return 初始参数对象
     */
    public static InitialParameters estimateInitialParameters(int quality) {
        // 基于质量分数调整初始参数 - 更加温和的调整
        double stability = INITIAL_STABILITY * (1.0 + quality * 0.15); // 稳定性调整幅度减小
        double difficulty = INITIAL_DIFFICULTY + (1.5 - quality) * 0.08; // 难度调整幅度减小
        double retrievability = 0.85 + quality * 0.15; // 检索能力调整幅度减小
        
        return InitialParameters.builder()
            .stability(stability)
            .difficulty(Math.max(MIN_DIFFICULTY, Math.min(MAX_DIFFICULTY, difficulty)))
            .retrievability(Math.max(MIN_RETRIEVABILITY, Math.min(MAX_RETRIEVABILITY, retrievability)))
            .build();
    }
    
    /**
     * 获取标准间隔
     * 
     * @param reviewCount 复习次数
     * @return 标准间隔（天）
     */
    public static int getStandardInterval(int reviewCount) {
        int index = Math.min(reviewCount, INTERVAL_DAYS.length - 1);
        return INTERVAL_DAYS[index];
    }
    
    /**
     * 验证算法参数是否在合理范围内
     * 
     * @param stability 稳定性
     * @param difficulty 难度
     * @param retrievability 检索能力
     * @return 是否有效
     */
    public static boolean validateParameters(double stability, double difficulty, double retrievability) {
        return stability > 0 && 
               difficulty >= MIN_DIFFICULTY && difficulty <= MAX_DIFFICULTY &&
               retrievability >= MIN_RETRIEVABILITY && retrievability <= MAX_RETRIEVABILITY;
    }
    
    @Data
    @Builder
    public static class InitialParameters {
        private double stability;
        private double difficulty;
        private double retrievability;
    }
}