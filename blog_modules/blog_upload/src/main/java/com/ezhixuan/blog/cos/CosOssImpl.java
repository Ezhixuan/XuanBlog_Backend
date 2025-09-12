package com.ezhixuan.blog.cos;

import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.handler.oss.AbstractObjectStorageService;
import com.ezhixuan.blog.handler.oss.OssModelEnum;
import com.ezhixuan.blog.handler.oss.OssProp;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;


/**
 * 腾讯云COS对象存储服务实现类 <br>
 * 提供基于腾讯云COS的对象存储功能，包括文件上传、下载和删除操作
 *
 * @author ezhixuan
 * @version 0.0.2beta
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CosOssImpl extends AbstractObjectStorageService {

  public static final OssModelEnum MODEL = OssModelEnum.COS;
  private final OssProp cosConfig;
  private COSClient client;

  /**
   * 获取当前使用的上传模式
   *
   * @return OssModelEnum COS存储模式枚举值
   */
  @Override
  public OssModelEnum getUploadModel() {
    return MODEL;
  }

  /**
   * 上传文件到腾讯云COS
   *
   * @param inputStream 文件输入流
   * @param targetPath 目标存储路径
   * @return String 上传后的文件访问URL
   */
  @Override
  public String doUpload(InputStream inputStream, String targetPath) {
    // 验证COS配置
    if (Objects.isNull(cosConfig)
        || Objects.isNull(cosConfig.getCosSecretId())
        || Objects.isNull(cosConfig.getCosSecretKey())
        || Objects.isNull(cosConfig.getCosRegion())
        || Objects.isNull(cosConfig.getCosBucketName())) {
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "检查COS配置");
    }

    try {
      COSClient cosClient = getCosClient();

      // 创建上传请求
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(cosConfig.getCosBucketName(), targetPath, inputStream, null);

      // 上传文件
      PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

      if (putObjectResult == null) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
      }

      String url =
          String.format(
              "https://%s.cos.%s.myqcloud.com/%s",
              cosConfig.getCosBucketName(), cosConfig.getCosRegion(), targetPath);
      log.info("文件上传成功：{}", url);
      return url;
    } catch (Exception e) {
      log.error("COS上传文件失败", e);
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
    }
  }

  /**
   * 从URL中提取COS对象名称
   *
   * @param urlStr 文件的URL地址
   * @return String 对象名称，如果提取失败则返回null
   */
  private String extractObjectName(String urlStr) {
    try {
      // COS的URL格式通常是：https://bucket.cos.region.myqcloud.com/key
      String prefix =
          String.format(
              "https://%s.cos.%s.myqcloud.com/",
              cosConfig.getCosBucketName(), cosConfig.getCosRegion());

      if (urlStr.startsWith(prefix)) {
        return urlStr.substring(prefix.length());
      } else {
        log.warn("URL格式不符合预期: {}", urlStr);
        // 尝试简单提取最后一部分作为对象名
        String[] parts = urlStr.split("/");
        return parts[parts.length - 1];
      }
    } catch (Exception e) {
      log.error("从URL提取对象名称失败", e);
      return null;
    }
  }

  /**
   * 删除COS中的文件
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

      COSClient cosClient = getCosClient();

      // 删除文件
      DeleteObjectRequest deleteObjectRequest =
          new DeleteObjectRequest(cosConfig.getCosBucketName(), objectName);
      cosClient.deleteObject(deleteObjectRequest);

      log.info("COS文件删除成功 url={}, objectName={}", urlStr, objectName);
      return true;
    } catch (Exception e) {
      log.error("COS删除文件失败 url={}", urlStr, e);
      return false;
    }
  }

  /**
   * 获取COS客户端实例
   * 如果客户端尚未初始化，则根据配置信息创建新的客户端实例
   *
   * @return COSClient COS客户端实例
   */
  private COSClient getCosClient() {
    if (Objects.nonNull(client)) {
      return client;
    }

    // 初始化用户身份信息
    COSCredentials cred =
        new BasicCOSCredentials(cosConfig.getCosSecretId(), cosConfig.getCosSecretKey());

    // 设置bucket的地域
    Region region = new Region(cosConfig.getCosRegion());

    // 初始化客户端配置
    ClientConfig clientConfig = new ClientConfig(region);

    // 生成cos客户端
    client = new COSClient(cred, clientConfig);
    return client;
  }

  /**
   * 判断给定的URL是否属于腾讯云COS存储服务<br>
   * 通过检查URL是否包含COS存储桶和区域信息来判断
   *
   * @since 0.0.2beta
   * @param url 待匹配的文件URL地址
   * @return boolean 如果URL属于当前COS存储服务则返回true，否则返回false
   */
  @Override
  public boolean matchUrl(String url) {
    return url.contains(
        String.format(
            "https://%s.cos.%s.myqcloud.com/",
            cosConfig.getCosBucketName(), cosConfig.getCosRegion()));
  }
}
