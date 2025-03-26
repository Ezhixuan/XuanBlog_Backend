package com.ezhixuan.xuanblog_backend.aop;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson2.JSON;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.utils.RedisUtil;
import com.github.benmanes.caffeine.cache.Caffeine;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

/**
 * 缓存
 */
@Aspect
@Component
public class CacheInterceptor {

    @Resource
    private RedisUtil redisUtil;

    final String preKey = "blog:";

    private final com.github.benmanes.caffeine.cache.Cache<String, Object> LOCAL_CACHE =
        Caffeine.newBuilder().initialCapacity(1024).maximumSize(10000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    @SneakyThrows
    @Around("@annotation(cache)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, Cache cache) {
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        String key = DigestUtils.md5DigestAsHex(JSON.toJSONString(args).getBytes());
        key = preKey + name + ":" + key;

        Object o = LOCAL_CACHE.getIfPresent(key);

        if (Objects.nonNull(o)) {
            if (o instanceof BaseResponse) {
                return o;
            } else {
                return R.success(o);
            }
        } else {
            o = redisUtil.get(key);
        }
        if (Objects.nonNull(o)) {
            if (o instanceof BaseResponse) {
                return o;
            } else {
                return R.success(o);
            }
        }

        Object res = joinPoint.proceed();
        long expireTime = cache.expireTime() + RandomUtil.randomLong(300);
        if (res instanceof BaseResponse) {
            BaseResponse response = (BaseResponse)res;
            Object data = response.getData();
            LOCAL_CACHE.put(key, data);
            redisUtil.set(key, data, expireTime);
        } else {
            LOCAL_CACHE.put(key, res);
            redisUtil.set(key, res, expireTime);
        }
        return res;
    }
}
