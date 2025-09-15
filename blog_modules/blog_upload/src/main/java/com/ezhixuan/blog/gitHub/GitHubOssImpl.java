package com.ezhixuan.blog.gitHub;

import cn.hutool.json.JSONObject;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.handler.oss.AbstractObjectStorageService;
import com.ezhixuan.blog.handler.oss.OssModelEnum;
import com.ezhixuan.blog.handler.oss.OssProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.Objects;

/**
 * GitHub对象存储服务实现类 <br>
 * 提供基于GitHub的对象存储功能，包括文件上传和删除操作<br>
 * 通过GitHub API实现文件的上传和删除，并使用jsDelivr CDN加速文件访问
 *
 * @author ezhixuan
 * @version 0.0.2beta
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubOssImpl extends AbstractObjectStorageService {

  public static final OssModelEnum MODEL = OssModelEnum.GITHUB;
  private final OssProp gitHubConfig;

  /**
   * 获取当前使用的上传模式
   *
   * @return OssModelEnum GitHub存储模式枚举值
   */
  @Override
  public OssModelEnum getUploadModel() {
    return MODEL;
  }

  /**
   * 上传文件到GitHub <br>
   * 立即返回通过jsDelivr CDN加速的访问URL，实际上传操作在虚拟线程中异步执行以优化用户体验<br>
   * 如果URL已存在则直接返回，避免重复上传；如果文件不存在，则异步创建新文件
   *
   * @param inputStream 文件输入流
   * @param targetPath 目标存储路径
   * @return String 文件访问URL（立即返回，上传异步进行）
   * @throws BusinessException 当GitHub配置不正确时抛出
   */
  @Override
  public String doUpload(InputStream inputStream, String targetPath) {
    if (Objects.isNull(gitHubConfig)
        || Objects.isNull(gitHubConfig.getGithubToken())
        || Objects.isNull(gitHubConfig.getGithubRepo())
        || Objects.isNull(gitHubConfig.getGithubBranch())) {
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "检查GitHub配置");
    }

    String repo = gitHubConfig.getGithubRepo();
    String branch = gitHubConfig.getGithubBranch();
    String token = gitHubConfig.getGithubToken();

    // 构造确定的URL
    String url = "https://cdn.jsdelivr.net/gh/" + repo + "@" + branch + "/" + targetPath;

    try {
      // 将输入流转换为字节数组，避免异步执行时流被关闭
      byte[] fileBytes = inputStreamToBytes(inputStream);

      // 使用虚拟线程异步执行上传操作
      Thread.ofVirtual().start(() -> {
        try {
          String apiUrl = "https://api.github.com/repos/" + repo + "/contents/" + targetPath;
          RestTemplate restTemplate = new RestTemplate();

          // 设置请求头
          HttpHeaders headers = new HttpHeaders();
          headers.set("Authorization", "token " + token);
          headers.set("Accept", "application/vnd.github.v3+json");
          headers.setContentType(MediaType.APPLICATION_JSON);

          // 先检查文件是否已存在
          try {
            HttpEntity<String> getEntity = new HttpEntity<>(headers);
            ResponseEntity<String> getResponse =
                restTemplate.exchange(apiUrl, HttpMethod.GET, getEntity, String.class);

            if (getResponse.getStatusCode().is2xxSuccessful()) {
              // 文件已存在，记录日志
              log.info("GitHub异步检查：文件已存在 targetPath={}", targetPath);
              return;
            }
          } catch (Exception e) {
            // 文件不存在或其他错误，继续创建新文件
            log.info("GitHub异步检查：文件不存在，将进行创建操作 targetPath={}", targetPath);
          }

          // 构造请求体（仅用于创建新文件）
          JSONObject body = new JSONObject();
          body.set("message", "Upload image via Java");
          body.set("content", bytesToBase64(fileBytes));
          body.set("branch", branch);

          HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

          ResponseEntity<String> response =
              restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);

          if (response.getStatusCode().is2xxSuccessful()) {
            log.info("GitHub异步上传成功：{}", url);
          } else {
            log.error("GitHub异步上传失败 response={}", response);
          }
        } catch (Exception e) {
          log.error("GitHub异步上传失败：{}", url, e);
        }
      });

      // 立即返回URL
      log.info("GitHub文件URL已生成，异步上传中：{}", url);
      return url;
    } catch (Exception e) {
      log.error("GitHub上传准备失败", e);
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传准备失败: " + e.getMessage());
    }
  }

  /**
   * 删除GitHub中的文件 <br>
   * 通过GitHub API删除指定URL对应的文件
   *
   * @param urlStr 文件的URL地址
   * @return boolean 删除是否成功
   */
  @Override
  public boolean doDelete(String urlStr) {
    try {
      // 从URL中提取文件路径
      String repo = gitHubConfig.getGithubRepo();
      String branch = gitHubConfig.getGithubBranch();
      String token = gitHubConfig.getGithubToken();

      // 构造GitHub API URL
      // cdn.jsdelivr.net/gh/user/repo@branch/path/to/file
      String prefix = "https://cdn.jsdelivr.net/gh/" + repo + "@" + branch + "/";
      if (!urlStr.startsWith(prefix)) {
        log.error("文件URL格式不正确 url={}", urlStr);
        return false;
      }

      String filePath = urlStr.substring(prefix.length());

      // 构造获取文件信息的API URL
      String fileInfoUrl = "https://api.github.com/repos/" + repo + "/contents/" + filePath;

      // 获取文件的SHA值
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "token " + token);
      headers.set("Accept", "application/vnd.github.v3+json");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<String> fileInfoResponse =
          restTemplate.exchange(fileInfoUrl, HttpMethod.GET, entity, String.class);

      if (!fileInfoResponse.getStatusCode().is2xxSuccessful()) {
        log.error("获取文件信息失败 response={}", fileInfoResponse);
        return false;
      }

      // 解析响应获取SHA值
      JSONObject fileInfo = new JSONObject(fileInfoResponse.getBody());
      String sha = fileInfo.getStr("sha");

      if (sha == null || sha.isEmpty()) {
        log.error("无法获取文件SHA值");
        return false;
      }

      // 构造删除文件的API URL
      String deleteUrl = "https://api.github.com/repos/" + repo + "/contents/" + filePath;

      // 构造删除请求体
      JSONObject deleteBody = new JSONObject();
      deleteBody.set("message", "Delete image via Java");
      deleteBody.set("sha", sha);
      deleteBody.set("branch", branch);

      HttpHeaders deleteHeaders = new HttpHeaders();
      deleteHeaders.set("Authorization", "token " + token);
      deleteHeaders.set("Accept", "application/vnd.github.v3+json");
      deleteHeaders.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> deleteEntity = new HttpEntity<>(deleteBody.toString(), deleteHeaders);

      ResponseEntity<String> deleteResponse =
          restTemplate.exchange(
              URI.create(deleteUrl), HttpMethod.DELETE, deleteEntity, String.class);

      if (!deleteResponse.getStatusCode().is2xxSuccessful()) {
        log.error("删除文件失败 response={}", deleteResponse);
        return false;
      }

      return true;
    } catch (Exception e) {
      log.error("删除GitHub文件失败 url={}", urlStr, e);
      return false;
    }
  }

  /**
   * 将输入流转换为字节数组<br>
   * 用于异步上传时避免流被提前关闭的问题
   *
   * @param inputStream 文件输入流
   * @return byte[] 文件字节数组
   * @throws IOException 当IO操作失败时抛出
   */
  private byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
    try (inputStream; ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }
      return byteArrayOutputStream.toByteArray();
    }
  }

  /**
   * 将字节数组转换为Base64编码字符串<br>
   * 用于将文件内容编码为GitHub API所需的格式
   *
   * @param bytes 文件字节数组
   * @return String Base64编码的文件内容
   */
  private String bytesToBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * 判断给定的URL是否属于GitHub存储服务<br>
   * 通过检查URL是否包含COS存储桶和区域信息来判断
   *
   * @since 0.0.2beta
   * @param url 待匹配的文件URL地址
   * @return boolean 如果URL属于当前GitHub存储服务则返回true，否则返回false
   */
  @Override
  public boolean matchUrl(String url) {
    return url.contains(
        String.format("%s@%s", gitHubConfig.getGithubRepo(), gitHubConfig.getGithubBranch()));
  }
}
