package com.example.javademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void saveData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 新增方法：删除数据
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    // 保存消息到Redis列表中
    public void saveMessage(String message) {
        stringRedisTemplate.opsForList().rightPush("chat:messages", message);
        // 限制列表长度，例如只保留最近100条消息
        stringRedisTemplate.opsForList().trim("chat:messages", 0, 99);
    }

    public void clearChatHistory() {
        stringRedisTemplate.delete("chat:messages");
    }

    // 获取Redis列表中的所有消息
    public List<String> getMessageHistory() {
        return new ArrayList<>(stringRedisTemplate.opsForList().range("chat:messages", 0, -1));
    }
}