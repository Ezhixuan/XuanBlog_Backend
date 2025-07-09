package com.ezhixuan.blog.test;

import com.ezhixuan.blog.TestApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Ezhixuan
 * @version 1.0.0
 * @Description 基础测试类，提供 Spring Boot 测试环境
 * @Date 2025-07-09 18:48
 */
@SpringBootTest(classes = TestApplication.class, properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
public class BaseTest {
}
