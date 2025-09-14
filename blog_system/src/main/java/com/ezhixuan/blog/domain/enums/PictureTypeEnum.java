package com.ezhixuan.blog.domain.enums;

import lombok.Getter;

/**
 * 图片上传类型枚举
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Getter
public enum PictureTypeEnum {
  ARTICLE_CONTENT(1, "文章内容"),
  ARTICLE_COVER(2, "文章封面"),
  USER_AVATAR(3, "用户头像");

  private final Integer value;
  private final String desc;

  PictureTypeEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }
}
