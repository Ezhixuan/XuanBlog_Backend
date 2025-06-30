package com.ezhixuan.blog.utils;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/*
redis lua脚本工具
 */
public class RedisLuaScriptUtil {

    public static final RedisScript<Boolean> THUMB_SCRIPT = new DefaultRedisScript<>("""
            local blogThumbKey = KEYS[1]
            local tempThumbKey = KEYS[2]
            local userId = ARGV[1]
            local articleId = ARGV[2]
            local newNumber
            
            local oldNumber = tonumber(redis.call('hget', blogThumbKey, userId) or 0);
            local tempNumber = tonumber(redis.call('hget', tempThumbKey, articleId) or 0);
            

            if oldNumber == 1 then
                oldNumber = 0
                newNumber = tempNumber - 1;
            else
                oldNumber = 1
                newNumber = tempNumber + 1;
            end
           
            redis.call('hset', blogThumbKey, userId, oldNumber);
            redis.call('hset', tempThumbKey, articleId, newNumber);
            
            return oldNumber;
            """, Boolean.class);
}
