package com.ezhixuan.blog.handler.oss;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 对象存储管理器 <br>
 * 负责管理多种对象存储服务（如本地存储、MinIO、GitHub、腾讯云COS等）的初始化和实例获取
 *
 * @author ezhixuan
 * @version 0.0.3beta
 * @since 0.0.2beta
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssManager {

  private static OssModelEnum type;
  private static List<OssModelEnum> types;
  private final List<ObjectStorageService> serviceList;
  private final OssProp prop;

  /** 初始化对象存储服务 在Spring容器启动时自动执行，用于初始化并确定当前使用的存储服务类型 */
  @PostConstruct
  public void init() {
    log.info("===========图片上传服务初始化===================");
    if (!isEmpty(serviceList)) {
      initializeAvailableTypes();
      initializeCurrentType();
      log.info("当前图片上传服务为：{}", type);
      log.info("===========图片上传服务初始化完成===================");
    } else {
      log.error("图片上传服务未初始化");
    }
  }

  /**
   * 获取当前配置的对象存储服务实例
   *
   * @return ObjectStorageService 当前使用的对象存储服务实例
   * @throws RuntimeException 当没有可用的对象存储服务时抛出异常
   */
  public ObjectStorageService getInstance() {
    return serviceList.stream()
        .filter(service -> Objects.equals(service.getUploadModel(), type))
        .findAny()
        .orElseThrow(() -> new RuntimeException("图片上传功能暂时停止提供"));
  }

  /**
   * 根据指定的对象存储模型获取对应的服务实例
   *
   * @param ossModel 对象存储模型枚举值
   * @return ObjectStorageService 对应的对象存储服务实例
   * @throws RuntimeException 当没有找到匹配的对象存储服务时抛出异常
   */
  public ObjectStorageService getInstance(OssModelEnum ossModel) {
    return serviceList.stream()
        .filter(service -> Objects.equals(service.getUploadModel(), ossModel))
        .findAny()
        .orElseThrow(() -> new RuntimeException("图片上传功能暂时停止提供"));
  }

  /**
   * 根据URL删除文件<br>
   * 通过匹配URL找到对应的存储服务，并执行删除操作
   *
   * @since 0.0.3beta
   * @param url 需要删除的文件URL地址
   * @return boolean 删除是否成功
   */
  public boolean doDelete(String url) {
    try {
      return serviceList.stream()
          .filter(service -> service.matchUrl(url))
          // 一般只会有一个,但是为了不错误,同时 url 也是唯一的,也不会导致误删
          .map(service -> service.doDelete(url))
          .filter(deleted -> deleted)
          .findAny()
          .orElse(false);
    } catch (Exception exception) {
      log.error("删除文件失败", exception);
      return false;
    }
  }

  /**
   * 注册并切换到指定的对象存储模型
   *
   * @param model 要切换到的存储模型名称
   * @return boolean 注册是否成功
   */
  public boolean register(String model) {
    if (isNull(model)) {
      return false;
    }
    try {
      initializeAvailableTypes();
      types.stream()
          .filter(registeredModel -> Objects.equals(registeredModel.getModel(), model))
          .findFirst()
          .ifPresent(useModel -> type = useModel);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 获取所有可用的对象存储类型
   *
   * @return List<OssModelEnum> 可用的对象存储类型列表
   */
  public List<OssModelEnum> getAvailableType() {
    if (isEmpty(serviceList)) {
      return Collections.emptyList();
    } else if (isEmpty(types)) {
      initializeAvailableTypes();
    }
    return new ArrayList<>(types);
  }

  /** 初始化可用的存储类型列表 通过扫描已注册的服务实例提取其支持的存储类型 */
  private void initializeAvailableTypes() {
    if (isEmpty(serviceList)) {
      return;
    }
    types = serviceList.stream().map(ObjectStorageService::getUploadModel).toList();
  }

  /**
   * 初始化当前使用的存储类型<br>
   * 根据配置文件中的设置确定当前应使用的存储类型，如果未配置或配置无效则使用第一个可用的服务
   */
  private void initializeCurrentType() {
    if (isEmpty(serviceList)) {
      return;
    }
    type =
        serviceList.stream()
            .filter(service -> Objects.equals(service.getUploadModel().getModel(), prop.getType()))
            .findFirst()
            .orElseGet(serviceList::getFirst)
            .getUploadModel();
  }
}
