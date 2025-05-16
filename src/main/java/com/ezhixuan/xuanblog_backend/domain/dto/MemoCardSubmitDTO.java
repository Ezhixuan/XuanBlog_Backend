package com.ezhixuan.xuanblog_backend.domain.dto;

import java.time.LocalDateTime;

import com.ezhixuan.xuanblog_backend.domain.constant.MemoConstant;
import com.ezhixuan.xuanblog_backend.domain.entity.memo.MemoCard;

import lombok.Data;

@Data
public class MemoCardSubmitDTO {

    /**
     * 卡片集 id
     */
    private Long deckId;

    /**
     * 问题
     */
    private String front;

    /**
     * 答案
     */
    private String back;

    public MemoCard toEntity() {
        MemoCard memoCard = new MemoCard();
        memoCard.setDeckId(this.deckId);
        memoCard.setFront(this.front);
        memoCard.setBack(this.back);
        LocalDateTime date = LocalDateTime.now();
        memoCard.setCreateTime(date);
        memoCard.setUpdateTime(date);
        memoCard.setReviewInterval(MemoConstant.INIT_REVIEW_INTERVAL);
        memoCard.setNextReviewDate(date.plusMinutes(MemoConstant.INIT_REVIEW_INTERVAL));
        memoCard.setQuality(0L);
        return memoCard;
    }
}
