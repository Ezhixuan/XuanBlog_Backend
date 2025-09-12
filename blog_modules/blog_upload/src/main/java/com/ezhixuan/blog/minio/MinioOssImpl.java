package com.ezhixuan.blog.minio;

import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.handler.oss.AbstractObjectStorageService;
import com.ezhixuan.blog.handler.oss.OssModelEnum;
import com.ezhixuan.blog.handler.oss.OssProp;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Objects;

/**
 * MinIO对象存储服务实现类 <br>
 * 提供基于MinIO的对象存储功能，包括文件上传、下载和删除操作<br>
 * 支持自动创建存储桶、文件上传、删除等操作
 *
 * @author ezhixuan
 * @version 0.0.2beta
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioOssImpl extends AbstractObjectStorageService {

  public static final OssModelEnum MODEL = OssModelEnum.MINIO;
  private final OssProp minioConfig;
  private MinioClient client;

  /**
   * 获取当前使用的上传模式
   *
   * @return OssModelEnum MinIO存储模式枚举值
   */
  @Override
  public OssModelEnum getUploadModel() {
    return MODEL;
  }

  /**
   * 上传文件到MinIO<br>
   * 如果存储桶不存在会自动创建，上传成功后返回文件访问URL
   *
   * @param inputStream 文件输入流
   * @param targetPath 目标存储路径
   * @return String 上传后的文件访问URL
   * @throws BusinessException 当MinIO配置不正确或上传失败时抛出
   */
  @Override
  public String doUpload(InputStream inputStream, String targetPath) {
    // 验证Minio配置
    if (Objects.isNull(minioConfig)
        || Objects.isNull(minioConfig.getMinioEndpoint())
        || Objects.isNull(minioConfig.getMinioBucket())
        || Objects.isNull(minioConfig.getMinioAccessKey())
        || Objects.isNull(minioConfig.getMinioSecretKey())) {
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "检查Minio配置");
    }

    try {
      MinioClient minioClient = getMinioClient();

      // 判断 bucket 是否存在
      if (!minioClient.bucketExists(
          BucketExistsArgs.builder().bucket(minioConfig.getMinioBucket()).build())) {
        minioClient.makeBucket(
            MakeBucketArgs.builder().bucket(minioConfig.getMinioBucket()).build());
      }

      minioClient.putObject(
          PutObjectArgs.builder().bucket(minioConfig.getMinioBucket()).object(targetPath).stream(
                  inputStream, inputStream.available(), -1)
              .build());

      String url =
          String.format(
              "%s/%s/%s", minioConfig.getMinioDomain(), minioConfig.getMinioBucket(), targetPath);
      log.info("文件上传成功：{}", url);
      return url;
    } catch (Exception e) {
      log.error("Minio上传文件失败", e);
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
    }
  }

  /**
   * 删除MinIO中的文件
   *
   * @param urlStr 文件的URL地址
   * @return boolean 删除是否成功
   */
  @Override
  public boolean doDelete(String urlStr) {
    try {
      // 从URL中提取对象名称
      String objectName = extractObjectName(urlStr);
      if (objectName == null) {
        log.error("无法从URL中提取对象名称 url={}", urlStr);
        return false;
      }

      // 获取Minio客户端
      MinioClient minioClient = getMinioClient();

      // 删除文件
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(minioConfig.getMinioBucket())
              .object(objectName)
              .build());

      log.info("Minio文件删除成功 url={}, objectName={}", urlStr, objectName);
      return true;
    } catch (Exception e) {
      log.error("Minio删除文件失败 url={}", urlStr, e);
      return false;
    }
  }

  /**
   * 从URL中提取MinIO对象名称<br>
   * 解析URL格式提取出存储在MinIO中的对象名称
   *
   * @param urlStr 文件的URL地址
   * @return String 对象名称，如果提取失败则返回null
   */
  private String extractObjectName(String urlStr) {
    // 解析对象名称，通常URL的格式是：endpoint/bucket/object?params
    try {
      // 去除查询参数
      String urlWithoutParams = urlStr.split("\\?")[0];

      // 去除endpoint和bucket部分
      String endpoint =
          minioConfig.getMinioEndpoint().endsWith("/")
              ? minioConfig.getMinioEndpoint()
              : minioConfig.getMinioEndpoint() + "/";
      String bucketPrefix = endpoint + minioConfig.getMinioBucket() + "/";

      if (urlWithoutParams.startsWith(bucketPrefix)) {
        return urlWithoutParams.substring(bucketPrefix.length());
      } else {
        // 如果URL格式不符合预期，可能需要更复杂的解析逻辑
        log.warn("URL格式不符合预期: {}", urlStr);
        // 尝试简单提取最后一部分作为对象名
        String[] parts = urlWithoutParams.split("/");
        return parts[parts.length - 1];
      }
    } catch (Exception e) {
      log.error("从URL提取对象名称失败", e);
      return null;
    }
  }

  /**
   * 获取MinIO客户端实例<br>
   * 如果客户端尚未初始化，则根据配置信息创建新的客户端实例
   *
   * @return MinioClient MinIO客户端实例
   */
  private MinioClient getMinioClient() {
    if (Objects.nonNull(client)) {
      return client;
    }
    client =
        MinioClient.builder()
            .endpoint(minioConfig.getMinioEndpoint())
            .credentials(minioConfig.getMinioAccessKey(), minioConfig.getMinioSecretKey())
            .build();
    return client;
  }

  /**
   * 判断给定的URL是否属于minio存储服务<br>
   * 通过检查URL是否包含COS存储桶和区域信息来判断
   *
   * @since 0.0.2beta
   * @param url 待匹配的文件URL地址
   * @return boolean 如果URL属于当前minio存储服务则返回true，否则返回false
   */
  @Override
  public boolean matchUrl(String url) {
    return url.contains(
        String.format("%s/%s", minioConfig.getMinioDomain(), minioConfig.getMinioBucket()));
  }
}
