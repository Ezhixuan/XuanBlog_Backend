package com.ezhixuan.blog.handler.oss;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传模式枚举类
 *
 * @author ezhixuan
 * @version 0.0.2beta
 */
@Getter
@AllArgsConstructor
public enum OssModelEnum {
  /** 本地上传 */
  LOCAL("local", "本地上传"),
  /** minio上传 */
  MINIO("minio", "minio上传"),
  /** github上传 */
  GITHUB("github", "github上传"),
  /** 腾讯云COS上传 */
  COS("cos", "腾讯云COS上传"),
  ;

  private final String model;

  private final String desc;
}
