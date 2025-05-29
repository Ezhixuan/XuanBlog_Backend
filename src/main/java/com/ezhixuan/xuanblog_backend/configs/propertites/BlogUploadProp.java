package com.ezhixuan.xuanblog_backend.configs.propertites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("blog.upload")
@Configuration
@Data
public class BlogUploadProp {

    private String type;

    private String githubToken;

    private String githubBranch;

    private String githubRepo;

    private String minioEndpoint;

    private String minioBucket;

    private String minioAccessKey;

    private String minioSecretKey;
}
