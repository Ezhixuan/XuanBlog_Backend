package com.ezhixuan.xuanblog_backend.service.impl;

import static com.ezhixuan.xuanblog_backend.domain.constant.MemoConstant.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.common.PageRequest;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoCardOperateDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.MemoQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoCard;
import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoDecks;
import com.ezhixuan.xuanblog_backend.domain.enums.MemoQualityEnum;
import com.ezhixuan.xuanblog_backend.domain.enums.MemoReviewIntervalEnum;
import com.ezhixuan.xuanblog_backend.domain.vo.MemoCardVO;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.mapper.MemoCardMapper;
import com.ezhixuan.xuanblog_backend.service.MemoCardService;
import com.ezhixuan.xuanblog_backend.service.MemoDecksService;

import cn.hutool.core.bean.BeanUtil;
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
        List<MemoCard> list = list(Wrappers.<MemoCard>lambdaQuery().le(MemoCard::getNextReviewDate, LocalDateTime.now())
            .eq(Objects.nonNull(deckId), MemoCard::getDeckId, deckId).last("limit 10"));
        return list.stream().map(item -> BeanUtil.copyProperties(item, MemoCardVO.class)).toList();
    }

    private LambdaQueryWrapper<MemoCard> queryWrapper(MemoQueryDTO queryDTO) {
        LambdaQueryWrapper<MemoCard> lqw = new LambdaQueryWrapper<>();
        lqw.eq(queryDTO.getDeckId() != null, MemoCard::getDeckId, queryDTO.getDeckId());

        lqw.orderByDesc(MemoCard::getUpdateTime);
        return lqw;
    }

    /**
     * 根据分数更新计划
     *
     * @author Ezhixuan
     * @param quality 分数
     * @param memoCard 被修改的卡片
     */
    private void updatePlanByQuality(Long quality, MemoCard memoCard) {
        ThrowUtils.throwIf(Objects.isNull(quality) || Objects.isNull(memoCard), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(quality > 5L || quality < 0L, ErrorCode.OPERATION_ERROR);

        Long id = memoCard.getId();
        Long repetitions = memoCard.getRepetitions(); // 当前的重复次数
        Double currentEaseFactor = memoCard.getEaseFactor(); // 卡片当前的EF
        Long currentReviewInterval = memoCard.getReviewInterval(); // 卡片当前的间隔

        ThrowUtils.throwIf(Objects.isNull(id) || Objects.isNull(repetitions) || Objects.isNull(currentEaseFactor)
            || Objects.isNull(currentReviewInterval), ErrorCode.PARAMS_ERROR);

        // 1. 无论回忆质量如何，都先根据本次回忆质量更新易度因子EF
        double calculatedNewEF = currentEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));

        if (calculatedNewEF < MIN_EASE_FACTOR) {
            memoCard.setEaseFactor(MIN_EASE_FACTOR);
        } else {
            memoCard.setEaseFactor(calculatedNewEF);
        }

        // 2. 根据回忆质量更新重复次数和复习间隔
        if (quality < 2L) { // 回忆失败或非常差
            memoCard.setRepetitions(0L); // 重置重复次数
            memoCard.setReviewInterval(INIT_REVIEW_INTERVAL); // 回到初始学习间隔
        } else { // 回忆成功 (quality = 2, 3, 4, or 5)
            repetitions++; // 增加重复次数
            memoCard.setRepetitions(repetitions);

            long newInterval;
            MemoReviewIntervalEnum reviewIntervalEnum = MemoReviewIntervalEnum.getEnum(repetitions); // 假设这是您预设的固定间隔枚举

            if (Objects.isNull(reviewIntervalEnum)) {
                // 当 repetitions 超出预设枚举范围后 (例如 repetitions >= 5)
                // 使用 SM-2 的乘法规则计算新间隔: I(n) = I(n-1) * EF
                // I(n-1) 是指卡片在本次被复习前的间隔，即 currentReviewInterval
                newInterval = Math.round(currentReviewInterval * memoCard.getEaseFactor()); // 使用更新后的EF
            } else {
                // 对于初期的几次复习，使用预设的固定间隔
                newInterval = reviewIntervalEnum.getReViewInterval();
            }

            if (repetitions > 0 && newInterval <= currentReviewInterval && Objects.isNull(reviewIntervalEnum)
                && quality >= 3) { // 只有在动态计算且质量较好时才强制增加
                newInterval = Math.round(currentReviewInterval * MIN_EASE_FACTOR); // 保证至少有最小幅度的增长
                if (newInterval <= currentReviewInterval)
                    newInterval = currentReviewInterval + 1; // 确保至少增加1分钟
            }

            if (newInterval >= MAX_REVIEW_INTERVAL) {
                newInterval = MAX_REVIEW_INTERVAL;
            }
            memoCard.setReviewInterval(newInterval);
        }

        // 4. 更新下一次复习时间、更新时间戳和本次复习质量
        memoCard.setNextReviewDate(LocalDateTime.now().plusMinutes(memoCard.getReviewInterval()));
        memoCard.setUpdateTime(LocalDateTime.now());
        memoCard.setQuality(quality); // 记录本次复习的质量

        // 5. 持久化更新
        updateById(memoCard);
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
