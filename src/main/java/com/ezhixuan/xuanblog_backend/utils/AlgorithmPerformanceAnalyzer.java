package com.ezhixuan.xuanblog_backend.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoCard;

import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 算法性能分析工具
 * 
 * 用于对比SM-2和SM-17算法的学习效果和性能指标
 * 
 * @author ezhixuan
 */
@UtilityClass
@Slf4j
public class AlgorithmPerformanceAnalyzer {

    /**
     * 算法性能指标
     */
    @Data
    public static class PerformanceMetrics {
        private String algorithmVersion;
        private int totalCards;
        private double avgDifficulty;
        private double avgStability;
        private double avgRetrievability;
        private double avgIntervalDays;
        private double avgRepetitions;
        private double avgLapses;
        private double successRate;
        private double retentionRate;
        private double efficiencyScore;
        private double predictionAccuracy;
    }

    /**
     * 对比结果
     */
    @Data
    public static class ComparisonResult {
        private PerformanceMetrics sm2Metrics;
        private PerformanceMetrics sm17Metrics;
        private double improvementPercentage;
        private String recommendation;
        private List<String> keyImprovement;
    }

    /**
     * 分析指定卡片列表的性能指标
     * 
     * @param cards 卡片列表
     * @return 性能指标
     */
    public static PerformanceMetrics analyzePerformance(List<MemoCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return new PerformanceMetrics();
        }

        PerformanceMetrics metrics = new PerformanceMetrics();
        
        // 基础统计
        metrics.setTotalCards(cards.size());
        metrics.setAvgRepetitions(cards.stream()
            .mapToLong(card -> card.getRepetitions() != null ? card.getRepetitions() : 0L)
            .average().orElse(0.0));
        
        // 计算成功率（质量分数>=3的比例）
        long successCount = cards.stream()
            .filter(card -> card.getQuality() != null && card.getQuality() >= 3)
            .count();
        metrics.setSuccessRate(cards.size() > 0 ? (double) successCount / cards.size() * 100 : 0.0);
        
        // 计算平均间隔（转换为天数）
        metrics.setAvgIntervalDays(cards.stream()
            .filter(card -> card.getReviewInterval() != null)
            .mapToLong(card -> card.getReviewInterval())
            .average().orElse(0.0) / (24.0 * 60.0)); // 分钟转天数
        
        // SM-17特有指标
        List<MemoCard> sm17Cards = cards.stream()
            .filter(card -> "SM17".equals(card.getAlgorithmVersion()))
            .collect(Collectors.toList());
        
        if (!sm17Cards.isEmpty()) {
            metrics.setAlgorithmVersion("SM17");
            metrics.setAvgDifficulty(sm17Cards.stream()
                .filter(card -> card.getDifficulty() != null)
                .mapToDouble(card -> card.getDifficulty())
                .average().orElse(0.5));
            
            metrics.setAvgStability(sm17Cards.stream()
                .filter(card -> card.getStability() != null)
                .mapToDouble(card -> card.getStability())
                .average().orElse(4.0));
            
            metrics.setAvgRetrievability(sm17Cards.stream()
                .filter(card -> card.getRetrievability() != null)
                .mapToDouble(card -> card.getRetrievability())
                .average().orElse(0.9));
            
            metrics.setAvgLapses(sm17Cards.stream()
                .filter(card -> card.getLapseCount() != null)
                .mapToLong(card -> card.getLapseCount())
                .average().orElse(0.0));
        } else {
            metrics.setAlgorithmVersion("SM2");
            // SM-2算法的默认值
            metrics.setAvgDifficulty(0.5);
            metrics.setAvgStability(4.0);
            metrics.setAvgRetrievability(0.9);
            metrics.setAvgLapses(0.0);
        }
        
        // 计算保持率（间隔>1天且质量>=3的比例）
        long retainedCount = cards.stream()
            .filter(card -> card.getReviewInterval() != null 
                         && card.getReviewInterval() > 24 * 60 // 大于1天
                         && card.getQuality() != null 
                         && card.getQuality() >= 3)
            .count();
        
        long eligibleCount = cards.stream()
            .filter(card -> card.getReviewInterval() != null 
                         && card.getReviewInterval() > 24 * 60)
            .count();
        
        metrics.setRetentionRate(eligibleCount > 0 ? (double) retainedCount / eligibleCount * 100 : 0.0);
        
        // 计算效率分数（成功率 × 平均间隔天数 / 平均重复次数）
        double efficiency = 0.0;
        if (metrics.getAvgRepetitions() > 0) {
            efficiency = (metrics.getSuccessRate() / 100.0) * 
                        metrics.getAvgIntervalDays() / 
                        metrics.getAvgRepetitions();
        }
        metrics.setEfficiencyScore(efficiency * 100); // 转换为百分比
        
        return metrics;
    }

    /**
     * 对比SM-2和SM-17算法的性能
     * 
     * @param allCards 所有卡片
     * @return 对比结果
     */
    public static ComparisonResult compareAlgorithms(List<MemoCard> allCards) {
        ComparisonResult result = new ComparisonResult();
        
        // 按算法版本分组
        Map<String, List<MemoCard>> cardsByAlgorithm = allCards.stream()
            .filter(card -> card.getAlgorithmVersion() != null)
            .collect(Collectors.groupingBy(card -> card.getAlgorithmVersion()));
        
        // 分析各算法性能
        List<MemoCard> sm2Cards = cardsByAlgorithm.getOrDefault("SM2", List.of());
        List<MemoCard> sm17Cards = cardsByAlgorithm.getOrDefault("SM17", List.of());
        
        PerformanceMetrics sm2Metrics = analyzePerformance(sm2Cards);
        PerformanceMetrics sm17Metrics = analyzePerformance(sm17Cards);
        
        result.setSm2Metrics(sm2Metrics);
        result.setSm17Metrics(sm17Metrics);
        
        // 计算改进百分比
        double improvementScore = calculateImprovementScore(sm2Metrics, sm17Metrics);
        result.setImprovementPercentage(improvementScore);
        
        // 生成建议
        result.setRecommendation(generateRecommendation(improvementScore, sm2Metrics, sm17Metrics));
        result.setKeyImprovement(identifyKeyImprovements(sm2Metrics, sm17Metrics));
        
        log.info("算法性能对比完成 - SM-2卡片:{}, SM-17卡片:{}, 改进度:{:.2f}%", 
                sm2Cards.size(), sm17Cards.size(), improvementScore);
        
        return result;
    }

    /**
     * 计算改进分数
     * 
     * @param sm2Metrics SM-2指标
     * @param sm17Metrics SM-17指标
     * @return 改进百分比
     */
    private static double calculateImprovementScore(PerformanceMetrics sm2Metrics, PerformanceMetrics sm17Metrics) {
        if (sm2Metrics.getTotalCards() == 0) {
            return 0.0;
        }
        
        // 综合考虑多个指标的改进
        double successRateImprovement = (sm17Metrics.getSuccessRate() - sm2Metrics.getSuccessRate()) / 100.0;
        double efficiencyImprovement = (sm17Metrics.getEfficiencyScore() - sm2Metrics.getEfficiencyScore()) / 100.0;
        double retentionImprovement = (sm17Metrics.getRetentionRate() - sm2Metrics.getRetentionRate()) / 100.0;
        
        // 加权平均（成功率40%，效率30%，保持率30%）
        double overallImprovement = successRateImprovement * 0.4 + 
                                   efficiencyImprovement * 0.3 + 
                                   retentionImprovement * 0.3;
        
        return overallImprovement * 100; // 转换为百分比
    }

    /**
     * 生成性能建议
     * 
     * @param improvementScore 改进分数
     * @param sm2Metrics SM-2指标
     * @param sm17Metrics SM-17指标
     * @return 建议文本
     */
    private static String generateRecommendation(double improvementScore, 
                                               PerformanceMetrics sm2Metrics, 
                                               PerformanceMetrics sm17Metrics) {
        
        if (improvementScore > 10) {
            return "SM-17算法显著优于SM-2算法，建议全面迁移至SM-17算法。" +
                   "预期可获得更高的学习效率和更好的记忆保持效果。";
        } else if (improvementScore > 5) {
            return "SM-17算法在多数指标上优于SM-2算法，建议逐步迁移。" +
                   "可以先对新创建的卡片使用SM-17算法。";
        } else if (improvementScore > 0) {
            return "SM-17算法有轻微改进，可以考虑迁移。" +
                   "建议继续观察和收集更多数据来评估长期效果。";
        } else if (improvementScore > -5) {
            return "两种算法效果相近，可以继续使用当前算法。" +
                   "建议对特定类型的学习材料尝试不同算法。";
        } else {
            return "当前数据显示SM-2算法效果更好，建议保持使用SM-2算法。" +
                   "可能需要调整SM-17算法的参数或等待更多数据积累。";
        }
    }

    /**
     * 识别关键改进点
     * 
     * @param sm2Metrics SM-2指标
     * @param sm17Metrics SM-17指标
     * @return 改进点列表
     */
    private static List<String> identifyKeyImprovements(PerformanceMetrics sm2Metrics, 
                                                       PerformanceMetrics sm17Metrics) {
        return List.of(
            String.format("成功率: %.1f%% → %.1f%% (%+.1f%%)", 
                         sm2Metrics.getSuccessRate(), 
                         sm17Metrics.getSuccessRate(),
                         sm17Metrics.getSuccessRate() - sm2Metrics.getSuccessRate()),
            
            String.format("效率分数: %.1f → %.1f (%+.1f)", 
                         sm2Metrics.getEfficiencyScore(), 
                         sm17Metrics.getEfficiencyScore(),
                         sm17Metrics.getEfficiencyScore() - sm2Metrics.getEfficiencyScore()),
            
            String.format("保持率: %.1f%% → %.1f%% (%+.1f%%)", 
                         sm2Metrics.getRetentionRate(), 
                         sm17Metrics.getRetentionRate(),
                         sm17Metrics.getRetentionRate() - sm2Metrics.getRetentionRate()),
            
            String.format("平均间隔: %.1f天 → %.1f天 (%+.1f天)", 
                         sm2Metrics.getAvgIntervalDays(), 
                         sm17Metrics.getAvgIntervalDays(),
                         sm17Metrics.getAvgIntervalDays() - sm2Metrics.getAvgIntervalDays()),
            
            String.format("平均难度: N/A → %.3f (智能评估)", 
                         sm17Metrics.getAvgDifficulty()),
            
            String.format("平均稳定性: N/A → %.1f天 (记忆强度)", 
                         sm17Metrics.getAvgStability())
        );
    }

    /**
     * 生成性能报告
     * 
     * @param result 对比结果
     * @return 格式化的报告文本
     */
    public static String generatePerformanceReport(ComparisonResult result) {
        StringBuilder report = new StringBuilder();
        
        report.append("=== SM算法性能对比报告 ===\n\n");
        
        // SM-2算法指标
        PerformanceMetrics sm2 = result.getSm2Metrics();
        report.append("SM-2算法指标:\n");
        report.append(String.format("- 卡片数量: %d\n", sm2.getTotalCards()));
        report.append(String.format("- 成功率: %.1f%%\n", sm2.getSuccessRate()));
        report.append(String.format("- 平均间隔: %.1f天\n", sm2.getAvgIntervalDays()));
        report.append(String.format("- 平均重复次数: %.1f\n", sm2.getAvgRepetitions()));
        report.append(String.format("- 效率分数: %.1f\n", sm2.getEfficiencyScore()));
        report.append("\n");
        
        // SM-17算法指标
        PerformanceMetrics sm17 = result.getSm17Metrics();
        report.append("SM-17算法指标:\n");
        report.append(String.format("- 卡片数量: %d\n", sm17.getTotalCards()));
        report.append(String.format("- 成功率: %.1f%%\n", sm17.getSuccessRate()));
        report.append(String.format("- 平均间隔: %.1f天\n", sm17.getAvgIntervalDays()));
        report.append(String.format("- 平均重复次数: %.1f\n", sm17.getAvgRepetitions()));
        report.append(String.format("- 平均难度: %.3f\n", sm17.getAvgDifficulty()));
        report.append(String.format("- 平均稳定性: %.1f天\n", sm17.getAvgStability()));
        report.append(String.format("- 平均检索能力: %.3f\n", sm17.getAvgRetrievability()));
        report.append(String.format("- 效率分数: %.1f\n", sm17.getEfficiencyScore()));
        report.append("\n");
        
        // 改进情况
        report.append("关键改进:\n");
        result.getKeyImprovement().forEach(improvement -> 
            report.append("- ").append(improvement).append("\n"));
        report.append("\n");
        
        // 总体评估
        report.append(String.format("总体改进度: %+.1f%%\n", result.getImprovementPercentage()));
        report.append("建议: ").append(result.getRecommendation()).append("\n");
        
        return report.toString();
    }
} 