package com.ezhixuan.xuanblog_backend.configs.propertites;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "blog")
@Configuration
@Data
public class BlogProp {

    private String salt;
}
