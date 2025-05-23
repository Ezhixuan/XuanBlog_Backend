package com.ezhixuan.xuanblog_backend.redis;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ezhixuan.xuanblog_backend.configs.propertites.BlogUploadProp;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

@SpringBootTest
public class MinioTest {

    @Resource
    private BlogUploadProp minioConfig;

    @Test
    @SneakyThrows
    void testMinioClient()  {
        System.out.println(minioConfig);
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioConfig.getMinioEndpoint())
                .credentials(minioConfig.getMinioAccessKey(), minioConfig.getMinioSecretKey())
                .build();
        List<Bucket> buckets = minioClient.listBuckets();
        System.out.println(buckets);
    }
}
