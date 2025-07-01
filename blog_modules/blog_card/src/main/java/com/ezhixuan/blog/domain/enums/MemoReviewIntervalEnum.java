package com.ezhixuan.blog.domain.enums;

import com.ezhixuan.blog.domain.constant.MemoConstant;
import lombok.Getter;

@Getter
public enum MemoReviewIntervalEnum {
    FIRST(MemoConstant.INIT_REVIEW_INTERVAL, 0L),
    SECOND(MemoConstant.SECOND_REVIEW_INTERVAL, 1L),
    THIRD(MemoConstant.THIRD_REVIEW_INTERVAL, 2L),
    FOURTH(MemoConstant.FOURTH_REVIEW_INTERVAL, 3L);


    private final Long reViewInterval;
    private final Long repetitions;

    MemoReviewIntervalEnum(Long reViewInterval, Long repetitions) {
        this.reViewInterval = reViewInterval;
        this.repetitions = repetitions;
    }

    public static MemoReviewIntervalEnum getEnum(Long repetitions) {
        for (MemoReviewIntervalEnum value : MemoReviewIntervalEnum.values()) {
            if (value.getRepetitions().equals(repetitions)) {
                return value;
            }
        }
        return null;
    }
}
