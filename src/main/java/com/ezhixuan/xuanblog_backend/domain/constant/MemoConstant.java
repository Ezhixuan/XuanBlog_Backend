package com.ezhixuan.xuanblog_backend.domain.constant;

/**
 * 记忆卡片复习步长
 */
public interface MemoConstant {

    Long INIT_REVIEW_INTERVAL = 20L;

    Long SECOND_REVIEW_INTERVAL = 60L;

    Long THIRD_REVIEW_INTERVAL = 180L;

    Long FOURTH_REVIEW_INTERVAL = 540L;

    Long MAX_REVIEW_INTERVAL = 43200L;

    double DEFAULT_EASE_FACTOR = 2.5;

    double MIN_EASE_FACTOR = 1.3;
}
