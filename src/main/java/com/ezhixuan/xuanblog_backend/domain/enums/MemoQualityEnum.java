package com.ezhixuan.xuanblog_backend.domain.enums;

import lombok.Getter;

@Getter
public enum MemoQualityEnum {

    HARD(2, 0L), MIDDLE(1, 3L), EASY(0, 5L);

    private final int type;
    private final long quality;

    MemoQualityEnum(int type, long quality) {
        this.type = type;
        this.quality = quality;
    }

    public static MemoQualityEnum getEnum(int type) {
        for (MemoQualityEnum memoQualityEnum : MemoQualityEnum.values()) {
            if (memoQualityEnum.getType() == type) {
                return memoQualityEnum;
            }
        }
        return null;
    }
}
