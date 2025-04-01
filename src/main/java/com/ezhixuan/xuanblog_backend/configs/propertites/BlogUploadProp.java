package com.ezhixuan.xuanblog_backend.configs.propertites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("blog.upload")
@Configuration
@Data
public class BlogUploadProp {

    private String type;

    private String token;

    private String branch;

    private String repo;
}
