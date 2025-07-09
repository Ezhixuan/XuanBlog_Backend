package com.ezhixuan.blog.test;

import com.ezhixuan.blog.config.RedissonConfig;
import org.springframework.context.annotation.Import;

/**
 * @author Ezhixuan
 * @version 1.0.0
 * @Description 仅包含 Redis 的测试基类
 * @Date 2025-07-09 18:50
 */
@Import(RedissonConfig.class)
public class RedisTest extends BaseTest {
}
