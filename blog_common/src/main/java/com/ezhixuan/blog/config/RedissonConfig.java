package com.ezhixuan.blog.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ezhixuan.blog.config.props.RedissonProp;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(RedissonProp prop) {
        Config config = new Config();
        config.useSingleServer()
              .setAddress(prop.getSingleServerConfig().getAddress())
              .setDatabase(prop.getSingleServerConfig().getDatabase())
              .setConnectionMinimumIdleSize(prop.getSingleServerConfig().getConnectionMinimumIdleSize())
              .setConnectionPoolSize(prop.getSingleServerConfig().getConnectionPoolSize())
              .setSubscriptionConnectionMinimumIdleSize(prop.getSingleServerConfig().getSubscriptionConnectionMinimumIdleSize())
              .setSubscriptionConnectionPoolSize(prop.getSingleServerConfig().getSubscriptionConnectionPoolSize());
        return Redisson.create(config);
    }
}
