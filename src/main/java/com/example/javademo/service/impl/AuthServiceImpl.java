package com.example.javademo.service.impl;

import com.example.javademo.dto.MenuDTO;
import com.example.javademo.mapper.AuthMapper;
import com.example.javademo.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AuthMapper authMapper;

    @Override
    public Object getPath(String username) {
        try {
            String path = authMapper.getPath(username);
            return objectMapper.readValue(path, MenuDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析失败: " + e.getMessage(), e);
        }
    }
}
