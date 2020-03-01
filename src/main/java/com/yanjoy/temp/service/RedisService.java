package com.yanjoy.temp.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public boolean set(String key, Object value) {
        boolean aBoolean = false;
        try {
            RedisSerializer redisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(redisSerializer);
            ValueOperations<String, Object> vo = redisTemplate.opsForValue();
            aBoolean = vo.setIfAbsent(key, value);
            if (aBoolean) {
                redisTemplate.expire(key, 30, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aBoolean;
    }

    public Object get(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public boolean delete(String key) {

        boolean delete = true;
        try {
            delete = redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delete;
    }
}