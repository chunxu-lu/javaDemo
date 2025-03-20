package com.example.javademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向Redis中存储键值对
     */
    public void saveData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据键从Redis中获取数据
     */
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}