package com.ezhixuan.blog.service.impl;

import static com.ezhixuan.blog.domain.constant.MemoConstant.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.common.PageRequest;
import com.ezhixuan.blog.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.blog.domain.dto.MemoQueryDTO;
import com.ezhixuan.blog.domain.entity.MemoCard;
import com.ezhixuan.blog.domain.entity.MemoDecks;
import com.ezhixuan.blog.domain.enums.MemoQualityEnum;
import com.ezhixuan.blog.domain.enums.MemoReviewIntervalEnum;
import com.ezhixuan.blog.domain.vo.MemoCardVO;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.mapper.MemoCardMapper;
import com.ezhixuan.blog.service.MemoCardService;
import com.ezhixuan.blog.service.MemoDecksService;
import com.ezhixuan.blog.utils.Sm17AlgorithmUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ezhixuan
 * @description 针对表【memo_card】的数据库操作Service实现
 * @createDate 2025-05-14 11:13:15
 */
@Service
@Slf4j
public class MemoCardServiceImpl extends ServiceImpl<MemoCardMapper, MemoCard> implements MemoCardService {

    @Resource
    private MemoDecksService decksService;

    /**
     * 对卡片进行操作 会根据 type 以及 useTime 对考核分进行计算 以变更下一次的记忆时间
     *
     * @param operateDTO 操作参数
     * @author Ezhixuan
     */
    @Override
    public void operate(MemoCardOperateDTO operateDTO) {
        ThrowUtils.throwIf(Objects.isNull(operateDTO) || Objects.isNull(operateDTO.getId()), ErrorCode.PARAMS_ERROR);
        Long id = operateDTO.getId();
        long useTime = operateDTO.getUseTime();
        int type = operateDTO.getType();
        ThrowUtils.throwIf(useTime < 0L || type > 3 || type < 0, ErrorCode.PARAMS_ERROR);

        MemoCard memoCard = getById(id);
        Long quality = calculateQuality(useTime, type);

        updatePlanByQuality(quality, memoCard);
    }

    /**
     * 查询vo
     *
     * @param queryDTO 查询 dto
     * @return 已筛选的卡片集信息
     * @author Ezhixuan
     */
    @Override
    public IPage<MemoCardVO> getMemoPageVOList(MemoQueryDTO queryDTO) {
        LambdaQueryWrapper<MemoCard> lqw = queryWrapper(queryDTO);
        IPage<MemoCard> iPage = queryDTO.toIPage();
        page(iPage, lqw);
        if (CollectionUtils.isEmpty(iPage.getRecords())) {
            return new Page<>();
        }
        Set<Long> deckIds = iPage.getRecords().stream().map(MemoCard::getDeckId).collect(Collectors.toSet());
        Map<Long, String> nameMap =
            decksService.listByIds(deckIds).stream().collect(Collectors.toMap(MemoDecks::getId, MemoDecks::getName));
        return PageRequest.convert(iPage, item -> {
            MemoCardVO memoCardVO = BeanUtil.copyProperties(item, MemoCardVO.class);
            memoCardVO.setDeckName(nameMap.get(item.getDeckId()));
            return memoCardVO;
        });
    }

    /**
     * 卡片测试 每次 10 张
     *
     * @return 测验卡片
     * @author Ezhixuan
     */
    @Override
    public List<MemoCardVO> test(Long deckId) {
        // 随机抽取 10 张卡片
        List<Long> ids = listObjs(Wrappers.<MemoCard>lambdaQuery().le(MemoCard::getNextReviewDate, LocalDateTime.now()).eq(Objects.nonNull(deckId), MemoCard::getDeckId, deckId).select(MemoCard::getId), o -> (long)o);
        HashSet<Long> idSet = new HashSet<>(10);
        if (ids.size() <= 10) {
            idSet.addAll(ids);
        }else {
            while (idSet.size() < 10) {
                Long id = ids.get(RandomUtil.randomInt(ids.size()));
                idSet.add(id);
            }
        }

        if (idSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<MemoCard> list = list(Wrappers.<MemoCard>lambdaQuery().in(MemoCard::getId, idSet));
        return list.stream().map(item -> BeanUtil.copyProperties(item, MemoCardVO.class)).toList();
    }

    private LambdaQueryWrapper<MemoCard> queryWrapper(MemoQueryDTO queryDTO) {
        LambdaQueryWrapper<MemoCard> lqw = new LambdaQueryWrapper<>();
        lqw.eq(queryDTO.getDeckId() != null, MemoCard::getDeckId, queryDTO.getDeckId());

        lqw.orderByDesc(MemoCard::getUpdateTime);
        return lqw;
    }

    /**
     * 根据分数更新计划 - SM-17算法增强版
     *
     * @author Ezhixuan
     * @param quality 分数
     * @param memoCard 被修改的卡片
     */
    private void updatePlanByQuality(Long quality, MemoCard memoCard) {
        ThrowUtils.throwIf(Objects.isNull(quality) || Objects.isNull(memoCard), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(quality > 5L || quality < 0L, ErrorCode.OPERATION_ERROR);

        // 检查算法版本，决定使用SM-17还是SM-2
        String algorithmVersion = memoCard.getAlgorithmVersion();
        if ("SM17".equals(algorithmVersion) || algorithmVersion == null) {
            updatePlanWithSm17(quality, memoCard);
        } else {
            updatePlanWithSm2(quality, memoCard);
        }
    }

    /**
     * 使用SM-17算法更新复习计划
     *
     * @param quality 质量分数
     * @param memoCard 记忆卡片
     */
    private void updatePlanWithSm17(Long quality, MemoCard memoCard) {
        Long id = memoCard.getId();
        LocalDateTime now = LocalDateTime.now();

        // 初始化SM-17算法所需的字段
        initializeSm17Fields(memoCard);

        // 计算实际的时间间隔（天数）
        long actualIntervalDays = 0;
        if (memoCard.getLastReviewTime() != null) {
            actualIntervalDays = Sm17AlgorithmUtil.calculateDaysBetween(memoCard.getLastReviewTime(), now);
        }

        // 计算当前的理论检索能力
        double currentStability = memoCard.getStability();
        double theoreticalRetrievability = Sm17AlgorithmUtil.calculateRetrievability(currentStability, actualIntervalDays);

        // 基于质量分数估算实际检索能力
        double actualRetrievability = Sm17AlgorithmUtil.estimateRetrievabilityFromQuality(quality);

        // 更新难度
        double newDifficulty = Sm17AlgorithmUtil.updateDifficulty(
            memoCard.getDifficulty(),
            quality,
            theoreticalRetrievability,
            actualRetrievability
        );

        // 更新稳定性
        double newStability = Sm17AlgorithmUtil.updateStability(
            currentStability,
            newDifficulty,
            actualRetrievability,
            quality
        );

        // 计算下次复习的最优间隔
        long newInterval = Sm17AlgorithmUtil.calculateOptimalInterval(
            newStability,
            DEFAULT_FORGETTING_INDEX
        );

        // 更新重复次数
        Long repetitions = memoCard.getRepetitions();
        if (quality < 2L) {
            // 回忆失败：重置重复次数，增加失败计数
            memoCard.setRepetitions(0L);
            memoCard.setLapseCount(memoCard.getLapseCount() == null ? 1L : memoCard.getLapseCount() + 1);
        } else {
            // 回忆成功：增加重复次数
            memoCard.setRepetitions(repetitions == null ? 1L : repetitions + 1);
        }

        // 更新卡片数据
        memoCard.setDifficulty(newDifficulty);
        memoCard.setStability(newStability);
        memoCard.setRetrievability(actualRetrievability);
        memoCard.setReviewInterval(newInterval);
        memoCard.setNextReviewDate(now.plusMinutes(newInterval));
        memoCard.setLastReviewTime(now);
        memoCard.setUpdateTime(now);
        memoCard.setQuality(quality);
        memoCard.setAlgorithmVersion("SM17");

        // 为兼容性保留易度因子计算
        updateEaseFactorForCompatibility(memoCard, quality);

        // 持久化更新
        updateById(memoCard);

        log.info("SM-17算法更新完成 - 卡片ID:{}, 质量:{}, 难度:{:.3f}, 稳定性:{:.2f}天, 间隔:{}分钟",
                id, quality, newDifficulty, newStability, newInterval);
    }

    /**
     * 使用传统SM-2算法更新复习计划（兼容模式）
     *
     * @param quality 质量分数
     * @param memoCard 记忆卡片
     */
    private void updatePlanWithSm2(Long quality, MemoCard memoCard) {
        Long id = memoCard.getId();
        Long repetitions = memoCard.getRepetitions();
        Double currentEaseFactor = memoCard.getEaseFactor();
        Long currentReviewInterval = memoCard.getReviewInterval();

        ThrowUtils.throwIf(Objects.isNull(id) || Objects.isNull(repetitions) || Objects.isNull(currentEaseFactor)
            || Objects.isNull(currentReviewInterval), ErrorCode.PARAMS_ERROR);

        // 1. 更新易度因子EF
        double calculatedNewEF = currentEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));

        if (calculatedNewEF < MIN_EASE_FACTOR) {
            memoCard.setEaseFactor(MIN_EASE_FACTOR);
        } else {
            memoCard.setEaseFactor(calculatedNewEF);
        }

        // 2. 根据回忆质量更新重复次数和复习间隔
        if (quality < 2L) {
            memoCard.setRepetitions(0L);
            memoCard.setReviewInterval(INIT_REVIEW_INTERVAL);
        } else {
            repetitions++;
            memoCard.setRepetitions(repetitions);

            long newInterval;
            MemoReviewIntervalEnum reviewIntervalEnum = MemoReviewIntervalEnum.getEnum(repetitions);

            if (Objects.isNull(reviewIntervalEnum)) {
                newInterval = Math.round(currentReviewInterval * memoCard.getEaseFactor());
            } else {
                newInterval = reviewIntervalEnum.getReViewInterval();
            }

            if (repetitions > 0 && newInterval <= currentReviewInterval && Objects.isNull(reviewIntervalEnum)
                && quality >= 3) {
                newInterval = Math.round(currentReviewInterval * MIN_EASE_FACTOR);
                if (newInterval <= currentReviewInterval)
                    newInterval = currentReviewInterval + 1;
            }

            if (newInterval >= MAX_REVIEW_INTERVAL) {
                newInterval = MAX_REVIEW_INTERVAL;
            }
            memoCard.setReviewInterval(newInterval);
        }

        // 3. 更新其他字段
        memoCard.setNextReviewDate(LocalDateTime.now().plusMinutes(memoCard.getReviewInterval()));
        memoCard.setUpdateTime(LocalDateTime.now());
        memoCard.setQuality(quality);
        memoCard.setAlgorithmVersion("SM2");

        // 4. 持久化更新
        updateById(memoCard);

        log.info("SM-2算法更新完成 - 卡片ID:{}, 质量:{}, 易度因子:{:.2f}, 间隔:{}分钟",
                id, quality, memoCard.getEaseFactor(), memoCard.getReviewInterval());
    }

    /**
     * 初始化SM-17算法所需的字段
     *
     * @param memoCard 记忆卡片
     */
    private void initializeSm17Fields(MemoCard memoCard) {
        // 初始化难度（基于历史表现估算）
        if (memoCard.getDifficulty() == null) {
            double initialDifficulty = 0.5; // 默认中等难度
            if (memoCard.getEaseFactor() != null) {
                // 从易度因子转换：EF越小，难度越大
                initialDifficulty = Math.max(0.0, Math.min(1.0, (4.0 - memoCard.getEaseFactor()) / 2.5));
            }
            memoCard.setDifficulty(initialDifficulty);
        }

        // 初始化稳定性
        if (memoCard.getStability() == null) {
            double initialStability = DEFAULT_INITIAL_STABILITY;
            if (memoCard.getQuality() != null) {
                initialStability = Sm17AlgorithmUtil.calculateInitialStability(memoCard.getQuality());
            }
            memoCard.setStability(initialStability);
        }

        // 初始化检索能力
        if (memoCard.getRetrievability() == null) {
            memoCard.setRetrievability(0.9); // 默认90%
        }

        // 初始化失败计数
        if (memoCard.getLapseCount() == null) {
            memoCard.setLapseCount(0L);
        }

        // 初始化上次复习时间
        if (memoCard.getLastReviewTime() == null) {
            memoCard.setLastReviewTime(memoCard.getUpdateTime() != null ?
                memoCard.getUpdateTime() : LocalDateTime.now().minusDays(1));
        }
    }

    /**
     * 为兼容性更新易度因子
     *
     * @param memoCard 记忆卡片
     * @param quality 质量分数
     */
    private void updateEaseFactorForCompatibility(MemoCard memoCard, Long quality) {
        // 从难度转换为易度因子：难度越大，易度因子越小
        double difficulty = memoCard.getDifficulty();
        double easeFactor = 4.0 - 2.5 * difficulty; // 映射到1.5-4.0范围
        easeFactor = Math.max(MIN_EASE_FACTOR, Math.min(4.0, easeFactor));

        // 根据质量分数微调
        if (quality < 3) {
            easeFactor *= 0.95; // 降低5%
        } else if (quality > 3) {
            easeFactor *= 1.05; // 提高5%
        }

        memoCard.setEaseFactor(Math.max(MIN_EASE_FACTOR, easeFactor));
    }

    private static final long TIME_EASY_QUICK_THRESHOLD = 10000;
    private static final long TIME_EASY_NORMAL_THRESHOLD = 15000;
    private static final long TIME_MIDDLE_QUICK_THRESHOLD = 20000;
    private static final long TIME_MIDDLE_NORMAL_THRESHOLD = 25000;
    private static final long TIME_MIDDLE_SLOW_THRESHOLD = 30000;

    /**
     * 根据耗时与结果计算权重
     *
     * @author Ezhixuan
     * @param useTime 耗时
     * @param type 记忆结果
     * @return 权重值
     */
    private Long calculateQuality(long useTime, int type) {
        MemoQualityEnum qualityEnum = MemoQualityEnum.getEnum(type);

        if (Objects.isNull(qualityEnum)) {
            log.error("未知的 type {}", type);
            return 0L;
        }

        long currentQuality = qualityEnum.getQuality();
        switch (qualityEnum) {
            case EASY:
                if (useTime <= TIME_EASY_QUICK_THRESHOLD) {
                    currentQuality = 5L;
                } else if (useTime <= TIME_EASY_NORMAL_THRESHOLD) {
                    currentQuality = 4L;
                } else {
                    currentQuality = 3L;
                }
                break;

            case MIDDLE:
                if (useTime <= TIME_MIDDLE_QUICK_THRESHOLD) {
                    currentQuality = 4L;
                } else if (useTime <= TIME_MIDDLE_NORMAL_THRESHOLD) {
                    currentQuality = 3L;
                } else if (useTime <= TIME_MIDDLE_SLOW_THRESHOLD) {
                    currentQuality = 2L;
                } else {
                    currentQuality = 1L;
                }
                break;

            case HARD:
                currentQuality = 0L;
                break;

            default:
                break;
        }
        return currentQuality;
    }
}
