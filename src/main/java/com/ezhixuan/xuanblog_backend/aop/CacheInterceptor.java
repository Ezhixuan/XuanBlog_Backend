package org.ezhixuan.xuan_picture_backend.aop;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ezhixuan.xuan_picture_backend.annotation.Cache;
import org.ezhixuan.xuan_picture_backend.common.BaseResponse;
import org.ezhixuan.xuan_picture_backend.common.ResultUtils;
import org.ezhixuan.xuan_picture_backend.utils.RedisUtil;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存
 */
@Aspect
@Component
public class CacheInterceptor {

    @Resource
    private RedisUtil redisUtil;

    final String preKey = "xPic:";

    private final com.github.benmanes.caffeine.cache.Cache<String, Object> LOCAL_CACHE = Caffeine.newBuilder().initialCapacity(1024).maximumSize(10000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();


    @SneakyThrows
    @Around("@annotation(cache)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, Cache cache) {
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        String key = DigestUtils.md5DigestAsHex(JSONUtil.toJsonStr(args).getBytes());
        key = preKey + name + ":" + key;

        Object o = LOCAL_CACHE.getIfPresent(key);

        if (Objects.nonNull(o)) {
            if (o instanceof  BaseResponse) {
                return o;
            }else {
                return ResultUtils.success(o);
            }
        }else {
            o = redisUtil.get(key);
        }
        if (Objects.nonNull(o)) {
            if (o instanceof  BaseResponse) {
                return o;
            }else {
                return ResultUtils.success(o);
            }
        }

        Object res = joinPoint.proceed();
        long expireTime = cache.expireTime() + RandomUtil.randomLong(300);
        if (res instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) res;
            Object data = response.getData();
            LOCAL_CACHE.put(key, data);
            redisUtil.set(key, data, expireTime);
        }else {
            LOCAL_CACHE.put(key, res);
            redisUtil.set(key, res, expireTime);
        }
        return res;
    }
}
