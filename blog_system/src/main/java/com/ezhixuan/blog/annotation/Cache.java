package com.ezhixuan.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    /**
     * 过期时间 默认5分钟
     */
    long expireTime() default 60L * 5;

    /**
     * 操作类型 默认插入
     */
    CacheOperateType operateType() default CacheOperateType.INSERT;

    /**
     * 用户自行设置key
     */
    String key() default "";

    enum CacheOperateType {
        INSERT, DELETE;
    }
}
