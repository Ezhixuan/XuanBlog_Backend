package com.ezhixuan.blog.handler.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@ConfigurationProperties("oss")
@Configuration
@Data
@Schema(description = "对象存储配置属性")
public class OssProp {

  @Schema(description = "存储类型(local:本地存储, minio:MinIO存储, github:GitHub存储, cos:腾讯云COS存储)")
  private String type;

  @Schema(description = "GitHub访问令牌")
  private String githubToken;

  @Schema(description = "GitHub分支名称")
  private String githubBranch;

  @Schema(description = "GitHub仓库名称")
  private String githubRepo;

  @Schema(description = "MinIO服务端点")
  private String minioEndpoint;

  @Schema(description = "MinIO存储桶名称")
  private String minioBucket;

  @Schema(description = "MinIO访问密钥")
  private String minioAccessKey;

  @Schema(description = "MinIO私有密钥")
  private String minioSecretKey;

  @Schema(description = "MinIO域名")
  private String minioDomain;

  @Schema(description = "腾讯云COS密钥ID")
  private String cosSecretId;

  @Schema(description = "腾讯云COS密钥Key")
  private String cosSecretKey;

  @Schema(description = "腾讯云COS区域")
  private String cosRegion;

  @Schema(description = "腾讯云COS存储桶名称")
  private String cosBucketName;

  public String getType() {
    return type.toLowerCase(Locale.ROOT);
  }
}
