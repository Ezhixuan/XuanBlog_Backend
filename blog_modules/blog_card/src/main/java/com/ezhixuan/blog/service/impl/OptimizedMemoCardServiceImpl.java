package com.ezhixuan.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.blog.domain.dto.MemoQueryDTO;
import com.ezhixuan.blog.domain.entity.MemoCard;
import com.ezhixuan.blog.domain.entity.MemoDecks;
import com.ezhixuan.blog.domain.enums.MemoQualityEnum;
import com.ezhixuan.blog.domain.vo.MemoCardVO;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.mapper.MemoCardMapper;
import com.ezhixuan.blog.service.MemoCardService;
import com.ezhixuan.blog.service.MemoDecksService;
import com.ezhixuan.blog.utils.SimpleIntervalAlgorithm;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优化后的记忆卡片服务实现
 * 使用简化的单一间隔重复算法
 *
 * 主要优化：
 * 1. 简化算法逻辑，移除SM-17/SM-2双算法复杂度
 * 2. 优化数据库查询，解决N+1问题
 * 3. 改进随机选择逻辑，使用数据库随机函数
 * 4. 简化质量分数计算逻辑
 * 5. 优化参数初始化和边界条件处理
 */
@Service
@Slf4j
public class OptimizedMemoCardServiceImpl extends ServiceImpl<MemoCardMapper, MemoCard>
    implements MemoCardService {

    @Resource
    private MemoDecksService decksService;

    // 质量分数计算阈值（毫秒）
    private static final long QUICK_THRESHOLD = 5000;    // 5秒
    private static final long NORMAL_THRESHOLD = 10000;  // 10秒
    private static final long SLOW_THRESHOLD = 20000;    // 20秒

    @Override
    public void operate(MemoCardOperateDTO operateDTO) {
        validateOperateDTO(operateDTO);

        MemoCard card = getById(operateDTO.getId());
        ThrowUtils.throwIf(card == null, ErrorCode.NOT_FOUND_ERROR, "卡片不存在");

        int quality = calculateQuality(operateDTO.getUseTime(), operateDTO.getType());

        updateCardAfterReview(card, quality, operateDTO.getUseTime());
    }

    /**
     * 验证操作参数
     */
    private void validateOperateDTO(MemoCardOperateDTO dto) {
        ThrowUtils.throwIf(Objects.isNull(dto) || Objects.isNull(dto.getId()),
            ErrorCode.PARAMS_ERROR, "参数不能为空");
        ThrowUtils.throwIf(dto.getUseTime() < 0,
            ErrorCode.PARAMS_ERROR, "用时不能为负数");
        ThrowUtils.throwIf(dto.getType() < 0 || dto.getType() > 2,
            ErrorCode.PARAMS_ERROR, "类型必须在0-2范围内");
    }

    /**
     * 计算质量分数（0-3分）
     * 简化逻辑，更加直观
     */
    private int calculateQuality(long useTime, int type) {
        MemoQualityEnum qualityEnum = MemoQualityEnum.getEnum(type);
        if (qualityEnum == null) {
            log.warn("未知的质量类型: {}", type);
            return 1; // 默认中等
        }

        // 基础质量分数转换（0-3范围）
        int baseQuality = (int) (qualityEnum.getQuality() / 2);

        // 根据用时微调
        switch (qualityEnum) {
            case EASY:
                if (useTime <= QUICK_THRESHOLD) {
                    return Math.min(3, baseQuality + 1); // 快速回答，质量+1
                } else if (useTime > NORMAL_THRESHOLD) {
                    return Math.max(1, baseQuality - 1); // 慢速回答，质量-1
                }
                break;

            case MIDDLE:
                if (useTime <= QUICK_THRESHOLD) {
                    return Math.min(3, baseQuality + 1);
                } else if (useTime > SLOW_THRESHOLD) {
                    return Math.max(0, baseQuality - 1);
                }
                break;

            case HARD:
                return 0; // 总是失败

            default:
                break;
        }

        return Math.max(0, Math.min(3, baseQuality)); // 确保在0-3范围内
    }

    /**
     * 复习后更新卡片
     * 使用简化的单一算法
     */
    private void updateCardAfterReview(MemoCard card, int quality, long timeSpent) {
        LocalDateTime now = LocalDateTime.now();

        // 初始化算法参数（如果是第一次复习）
        initializeAlgorithmParameters(card);

        // 获取当前参数
        double stability = Optional.ofNullable(card.getStability()).orElse(1.0);
        double difficulty = Optional.ofNullable(card.getDifficulty()).orElse(0.5);
        int reviewCount = Optional.ofNullable(card.getRepetitions()).orElse(0L).intValue();

        // 参数验证
        if (!SimpleIntervalAlgorithm.validateParameters(stability, difficulty,
            Optional.ofNullable(card.getRetrievability()).orElse(0.8))) {
            log.warn("算法参数异常，重新初始化 - 卡片ID:{}", card.getId());
            SimpleIntervalAlgorithm.InitialParameters params = SimpleIntervalAlgorithm.estimateInitialParameters(quality);
            stability = params.getStability();
            difficulty = params.getDifficulty();
        }

        // 使用简化算法更新参数
        double newStability = SimpleIntervalAlgorithm.updateStability(stability, difficulty, quality);
        double newDifficulty = SimpleIntervalAlgorithm.updateDifficulty(difficulty, quality);
        int newIntervalDays = SimpleIntervalAlgorithm.calculateNextInterval(
            newStability, newDifficulty, quality, reviewCount);

        // 更新复习计数
        int newReviewCount = quality < 2 ? 0 : reviewCount + 1;

        // 更新卡片数据
        card.setStability(newStability);
        card.setDifficulty(newDifficulty);
        card.setRetrievability(SimpleIntervalAlgorithm.calculateRetrievability(newStability, newIntervalDays));
        card.setRepetitions((long) newReviewCount);
        card.setLapseCount(quality < 2 ?
            Optional.ofNullable(card.getLapseCount()).orElse(0L) + 1 :
            Optional.ofNullable(card.getLapseCount()).orElse(0L));
        card.setNextReviewDate(now.plusDays(newIntervalDays));
        card.setLastReviewTime(now);
        card.setUpdateTime(now);
        card.setQuality((long) quality);

        // 更新数据库
        boolean updated = updateById(card);
        if (updated) {
            log.info("卡片复习更新成功 - ID:{}, 质量:{}, 稳定性:{:.1f}天, 难度:{:.2f}, 间隔:{}天",
                card.getId(), quality, newStability, newDifficulty, newIntervalDays);
        } else {
            log.error("卡片复习更新失败 - ID:{}", card.getId());
        }
    }

    /**
     * 初始化算法参数
     */
    private void initializeAlgorithmParameters(MemoCard card) {
        if (card.getStability() == null || card.getDifficulty() == null) {
            // 基于卡片历史估算初始参数
            int estimatedQuality = 2; // 默认中等质量
            if (card.getQuality() != null) {
                estimatedQuality = Math.max(0, Math.min(3, card.getQuality().intValue()));
            }

            SimpleIntervalAlgorithm.InitialParameters params =
                SimpleIntervalAlgorithm.estimateInitialParameters(estimatedQuality);

            if (card.getStability() == null) {
                card.setStability(params.getStability());
            }
            if (card.getDifficulty() == null) {
                card.setDifficulty(params.getDifficulty());
            }
            if (card.getRetrievability() == null) {
                card.setRetrievability(params.getRetrievability());
            }
            if (card.getLapseCount() == null) {
                card.setLapseCount(0L);
            }
            if (card.getLastReviewTime() == null) {
                card.setLastReviewTime(Optional.ofNullable(card.getCreateTime()).orElse(LocalDateTime.now()));
            }
        }
    }

    @Override
    public IPage<MemoCardVO> getMemoPageVOList(MemoQueryDTO queryDTO) {
        // 参数验证
        if (queryDTO == null) {
            return new Page<>();
        }

        // 构建查询条件
        LambdaQueryWrapper<MemoCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getDeckId() != null, MemoCard::getDeckId, queryDTO.getDeckId())
               .orderByDesc(MemoCard::getUpdateTime);

        // 执行分页查询
        IPage<MemoCard> cardPage = page(queryDTO.toPage(), wrapper);

        if (CollectionUtils.isEmpty(cardPage.getRecords())) {
            return new Page<>();
        }

        // 获取卡片集名称映射
        Set<Long> deckIds = cardPage.getRecords().stream()
            .map(MemoCard::getDeckId)
            .collect(Collectors.toSet());

        Map<Long, String> deckNameMap = decksService.listByIds(deckIds).stream()
            .collect(Collectors.toMap(MemoDecks::getId, MemoDecks::getName));

        // 转换为VO对象
        return cardPage.convert(card -> {
            MemoCardVO vo = BeanUtil.copyProperties(card, MemoCardVO.class);
            vo.setDeckName(deckNameMap.get(card.getDeckId()));
            return vo;
        });
    }

    @Override
    public List<MemoCardVO> test(Long deckId) {
        // 参数验证
        if (deckId != null && deckId <= 0) {
            return Collections.emptyList();
        }

        // 使用数据库随机函数优化性能
        List<MemoCardVO> cards = baseMapper.selectRandomCardsForReview(deckId, 10);

        log.debug("随机选择复习卡片 - deckId:{}, 数量:{}", deckId, cards.size());
        return cards;
    }

    @Override
    public boolean saveOrUpdate(MemoCard entity) {
        if (entity == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (entity.getId() == null) {
            // 新卡片
            entity.setCreateTime(now);
            entity.setNextReviewDate(now.plusDays(1)); // 默认1天后复习
            entity.setRepetitions(0L);
            entity.setLapseCount(0L);
            entity.setQuality(0L);
        }
        entity.setUpdateTime(now);

        return super.saveOrUpdate(entity);
    }
}
