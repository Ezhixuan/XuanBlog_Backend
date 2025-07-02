package com.ezhixuan.blog.minio;

import java.io.InputStream;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ezhixuan.blog.config.props.BlogUploadProp;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.handler.picture.PictureCommonUtil;
import com.ezhixuan.blog.handler.picture.PictureUploadHandler;
import com.ezhixuan.blog.handler.picture.UploadModel;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Minio图片服务实现类
 *
 * @author Ezhixuan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioPictureServiceImpl implements PictureUploadHandler {

    private final BlogUploadProp minioConfig;
    public static final UploadModel MODEL = UploadModel.builder().model("MINIO").desc("minio").build();

    /**
     * 优先级 默认 100 越小越优先执行
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 获取上传模型
     *
     * @return 实现者所属上传模型
     * @author Ezhixuan
     */
    @Override
    public UploadModel getUploadModel() {
        return MODEL;
    }

    /**
     * 上传图片
     *
     * @param inputStream 文件流
     * @param targetPath  目标路径
     * @param fileName    文件名
     * @return url
     * @author Ezhixuan
     */
    @Override
    public String doUpload(InputStream inputStream, String targetPath, String fileName) {
        // 验证Minio配置
        ThrowUtils.throwIf(Objects.isNull(minioConfig) || Objects.isNull(minioConfig.getMinioEndpoint())
            || Objects.isNull(minioConfig.getMinioBucket()) || Objects.isNull(minioConfig.getMinioAccessKey())
            || Objects.isNull(minioConfig.getMinioSecretKey()), ErrorCode.SYSTEM_ERROR, "检查Minio配置");

        try {
            MinioClient minioClient = getMinioClient();
            String objectName = PictureCommonUtil.pathName(targetPath, fileName);

            // 判断 bucket 是否存在
            if (!CLIENT.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getMinioBucket()).build())) {
                CLIENT.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getMinioBucket()).build());
            }
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getMinioBucket()).object(objectName)
                    .stream(inputStream, inputStream.available(), -1).contentType(PictureCommonUtil.getContentType(fileName))
                    .build());

            String url = String.format("%s/%s/%s", minioConfig.getMinioEndpoint(), minioConfig.getMinioBucket(), objectName);
            log.info("文件上传成功：{}", url);
            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件
     *
     * @param urlStr
     * @return 文件流
     * @author Ezhixuan
     */
    @Override
    public InputStream doDownload(String urlStr) {
        PictureCommonUtil.validatePicture(urlStr);

        try {
            // 从URL中提取对象名称
            String objectName = extractObjectName(urlStr);
            if (objectName == null) {
                ThrowUtils.exception(ErrorCode.SYSTEM_ERROR.getCode(), "无法从URL中提取对象名称");
                return null;
            }

            // 获取Minio客户端
            MinioClient minioClient = getMinioClient();

            // 获取文件输入流
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(minioConfig.getMinioBucket())
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            log.error("Minio下载文件失败", e);
            ThrowUtils.exception(ErrorCode.SYSTEM_ERROR.getCode(), "下载文件失败 url={}", urlStr);
            return null;
        }
    }

    /**
     * 从URL中提取对象名称
     *
     * @param urlStr URL字符串
     * @return 对象名称
     */
    private String extractObjectName(String urlStr) {
        // 解析对象名称，通常URL的格式是：endpoint/bucket/object?params
        try {
            // 去除查询参数
            String urlWithoutParams = urlStr.split("\\?")[0];

            // 去除endpoint和bucket部分
            String endpoint = minioConfig.getMinioEndpoint().endsWith("/")
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

    public MinioClient CLIENT;

    /**
     * 获取Minio客户端
     *
     * @return MinioClient实例
     */
    private MinioClient getMinioClient() {
        if (Objects.nonNull(CLIENT)) return CLIENT;
        CLIENT = MinioClient.builder()
            .endpoint(minioConfig.getMinioEndpoint())
            .credentials(minioConfig.getMinioAccessKey(), minioConfig.getMinioSecretKey())
            .build();
        return CLIENT;
    }
}
