package com.ezhixuan.blog.handler.picture;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片工厂
 *
 * @author ezhixuan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PictureFactory {

    private final List<PictureUploadHandler> serviceList;

    private static UploadModel type;
    private static List<UploadModel> types;
    private final BlogUploadProp prop;

    @PostConstruct
    public void init() {
        log.info("===========图片上传服务初始化===================");
        if (!isEmpty(serviceList)) {
            initTypes();
            initType();
            log.info("当前图片上传服务为：{}", type);
            log.info("===========图片上传服务初始化完成===================");
        } else {
            log.error("图片上传服务未初始化");
        }
    }

    public PictureUploadHandler getInstance() {
        return serviceList.stream().filter(service -> Objects.equals(service.getUploadModel(), type)).findAny()
            .orElseThrow(() -> new RuntimeException("图片上传功能暂时停止提供"));
    }

    public boolean register(String model) {
        if (isNull(model)) {
            return false;
        }
        try {
            initTypes();
            types.stream().filter(registeredModel -> Objects.equals(registeredModel.getModel(), model)).findFirst().ifPresent(useModel -> {
                type = useModel;;
            });
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public List<UploadModel> getAvailableType() {
        if (isEmpty(serviceList)) {
            return Collections.emptyList();
        } else if (isEmpty(types)) {
            initTypes();
        }
        return new ArrayList<>(types);
    }

    private void initTypes() {
        if (isEmpty(serviceList)) {
            return;
        }
        types = serviceList.stream().map(PictureUploadHandler::getUploadModel).toList();
    }

    private void initType() {
        if (isEmpty(serviceList)) {
            return;
        }
        type = serviceList.stream().filter(service -> Objects.equals(service.getUploadModel().getModel(), prop.getType())).findFirst()
            .orElseGet(() -> serviceList.get(0)).getUploadModel();
    }
}
