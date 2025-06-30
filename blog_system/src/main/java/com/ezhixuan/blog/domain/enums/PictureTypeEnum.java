package com.ezhixuan.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片类型枚举
 *
 * @author ezhixuan
 */
@Getter
@AllArgsConstructor
public enum PictureTypeEnum {

    /**
     * 博客内容图片
     */
    CONTENT(1, "博客内容图片"),

    /**
     * 博客封面图片
     */
    COVER(2, "博客封面图片"),

    /**
     * 博客用户头像
     */
    AVATAR(3, "博客用户头像");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static PictureTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PictureTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
} 