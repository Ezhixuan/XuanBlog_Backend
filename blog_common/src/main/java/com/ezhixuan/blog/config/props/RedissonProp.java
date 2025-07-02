package com.ezhixuan.blog.config.props;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.redisson")
@Configuration
@Data
public class RedissonProp {

    private SingleServerConfig singleServerConfig;

    @Data
    @NoArgsConstructor
    public static class SingleServerConfig {
        /**
         * redis地址
         */
        private String address = "redis://127.0.0.1:6379";

        /**
         * 数据库索引
         */
        private int database = 0;

        /**
         * 连接超时时间
         */
        private int timeout = 3000;

        /**
         * 最小空闲redis连接量
         */
        private int connectionMinimumIdleSize = 32;

        /**
         * 连接池大小
         */
        private int connectionPoolSize = 64;

        /**
         * 订阅连接池大小
         */
        private int subscriptionConnectionPoolSize = 50;

        /**
         * 订阅最小空闲redis连接量
         */
        private int subscriptionConnectionMinimumIdleSize = 1;
    }
}
