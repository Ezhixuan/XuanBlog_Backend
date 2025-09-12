package com.ezhixuan.blog.handler.oss;

import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * 抽象对象存储服务类 <br>
 * 提供对象存储服务的基础实现，包括文件下载和URL验证功能 <br>
 * 具体的存储服务实现类需要继承此类并实现抽象方法
 *
 * @author ezhixuan
 * @version 0.0.2beta
 */
@Slf4j
public abstract class AbstractObjectStorageService implements ObjectStorageService {

  public static final String HTTP = "http://";
  public static final String HTTPS = "https://";

  /**
   * 验证图片URL的有效性 <br>
   * 检查URL是否为空、格式是否正确以及协议是否为HTTP或HTTPS
   *
   * @param fileUrl 待验证的文件URL
   * @throws BusinessException 当URL为空、格式不正确或协议错误时抛出
   */
  public static void validatePicture(String fileUrl) {
    if (!StringUtils.hasText(fileUrl)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址不能为空");
    }

    try {
      // 验证 URL 格式
      URL url = URI.create(fileUrl).toURL();
    } catch (MalformedURLException e) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址格式不正确");
    }

    // 校验 URL 协议
    if (!fileUrl.startsWith(HTTP) && !fileUrl.startsWith(HTTPS)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址协议错误");
    }
  }

  /**
   * 从指定URL下载文件 <br>
   * 首先验证URL有效性，然后建立HTTP连接并获取输入流
   *
   * @param urlStr 文件的URL地址
   * @return InputStream 文件输入流
   * @throws BusinessException 当下载失败或网络异常时抛出
   */
  @Override
  public InputStream doDownload(String urlStr) {
    validatePicture(urlStr);
    try {
      URL url = URI.create(urlStr).toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(10000);
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "下载失败，请检查网络");
      }
      // 获取输入流
      return connection.getInputStream();
    } catch (IOException e) {
      log.error("下载文件失败 url={}", urlStr, e);
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败 url=" + urlStr);
    }
  }

  /**
   * 获取当前使用的上传模式
   *
   * @return OssModelEnum 上传模式枚举值
   */
  @Override
  public abstract OssModelEnum getUploadModel();

  /**
   * 上传文件
   *
   * @param inputStream 文件输入流
   * @param targetPath 目标存储路径
   * @return String 上传后的文件访问URL
   */
  @Override
  public abstract String doUpload(InputStream inputStream, String targetPath);

  /**
   * 删除文件
   *
   * @param urlStr 文件的URL地址
   * @return boolean 删除是否成功
   */
  @Override
  public abstract boolean doDelete(String urlStr);
}
