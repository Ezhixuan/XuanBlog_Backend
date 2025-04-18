package com.ezhixuan.xuanblog_backend.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import jakarta.annotation.Resource;
import lombok.Data;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key key
     * @param value 值
     */
    public <T> boolean set(String key, T value) {
        try {
            RedisInnerData tRedisInnerData = getRedisInnerData(value);
            redisTemplate.opsForValue().set(key, tRedisInnerData);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                RedisInnerData tRedisInnerData = getRedisInnerData(value);
                redisTemplate.opsForValue().set(key, tRedisInnerData, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        RedisInnerData innerData = (RedisInnerData)redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(innerData)) {
            String objJsonString = innerData.getObj();
            String className = innerData.getClassName();
            try {
                Class<?> elementClass = Class.forName(className);
                if (innerData.isList()) {
                    TypeReference<List<?>> typeReference = new TypeReference<>() {};
                    List<?> list = JSON.parseObject(objJsonString, typeReference.getType());
                    return list.stream().map(item -> {
                        JSONObject item1 = (JSONObject)item;
                        return item1.toJavaObject(elementClass);
                    }).toList();
                } else {
                    return JSON.parseObject(objJsonString, elementClass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private <T> RedisInnerData getRedisInnerData(T value) {
        RedisInnerData tRedisInnerData = new RedisInnerData();
        String name = value.getClass().getName();
        if (value instanceof List) {
            tRedisInnerData.setList(true);
            ((List<?>)value).stream().findAny().ifPresent(o -> {
                tRedisInnerData.setClassName(o.getClass().getName());
            });
        } else {
            tRedisInnerData.setClassName(name);
        }
        tRedisInnerData.setObj(JSON.toJSONString(value));
        return tRedisInnerData;
    }

    /**
     * 模糊匹配
     *
     * @param pattern key？
     * @return List<String> keys
     */
    public List<String> scan(String pattern) {
        List<String> result = new ArrayList<>();
        try (Cursor<byte[]> cursor =
            (Cursor<byte[]>)redisTemplate.execute((RedisCallback<Cursor<byte[]>>)redisConnection -> redisConnection
                .scan(ScanOptions.scanOptions().match(pattern + "*").count(1000).build()))) {
            while (cursor.hasNext()) {
                result.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public <T> T hGet(String key, String hKey) {
        Object value = redisTemplate.opsForHash().get(key, hKey);
        return (T)value;
    }

    public boolean hSet(String key, String hKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Data
    public static class RedisInnerData {
        private String obj;
        private String className;
        private boolean isList;
    }
}
