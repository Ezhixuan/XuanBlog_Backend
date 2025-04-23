package com.ezhixuan.xuanblog_backend.aop;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.ezhixuan.xuanblog_backend.annotation.Cache;
import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.constant.RedisKeyConstant;
import com.ezhixuan.xuanblog_backend.utils.RedisUtil;
import com.ezhixuan.xuanblog_backend.utils.SpELExplainUtil;
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

    private final static com.github.benmanes.caffeine.cache.Cache<String, Object> LOCAL_CACHE =
        Caffeine.newBuilder().initialCapacity(1024).maximumSize(10000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    @SneakyThrows
    @Around("@annotation(cache)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, Cache cache) {
        String key = getKey(joinPoint, cache);
        String resTypeName = ((MethodSignature) joinPoint.getSignature()).getReturnType().getName();
        Class<?> aClass = Class.forName(resTypeName);
        Object o = LOCAL_CACHE.getIfPresent(key);

        if (Objects.nonNull(o)) {
            if (aClass.equals(BaseResponse.class)) {
                return R.success(o);
            }
            return aClass.cast(o);
        } else {
            o = redisUtil.get(key);
        }
        if (Objects.nonNull(o)) {
            if (aClass.equals(BaseResponse.class)) {
                return R.success(o);
            }
            return o;
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

    private String getKey(ProceedingJoinPoint joinPoint, Cache cache) {
        String key = cache.key();
        if (StringUtils.hasText(key)) {
            return spELSupport(joinPoint, key);
        }

        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String suffix = DigestUtils.md5DigestAsHex(JSON.toJSONBytes(args));
        key = RedisKeyConstant.BLOG_PREFIX + signature.getName() + ":" + suffix;
        return key;
    }

    private String spELSupport(ProceedingJoinPoint joinPoint, String key) {
        SpELExplainUtil spELExplainUtil = new SpELExplainUtil();
        if (validKey(key)) {
            String[] split = key.split(":");
            for (int i = 0; i < split.length; i++) {
                split[i] = spELExplainUtil.explain(split[i], joinPoint);
            }
            key = String.join(":", split);
        } else {
            key = spELExplainUtil.explain(key, joinPoint);
        }
        return key;
    }

    public boolean validKey(String key) {
        String[] split = key.split(":");
        return split.length > 1;
    }

    public void cleanLocalCache(String key) {
        LOCAL_CACHE.invalidate(key);
        redisUtil.cleanCache(key);
    }
}
