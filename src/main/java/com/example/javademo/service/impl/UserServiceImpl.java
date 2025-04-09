package com.example.javademo.service.impl;

import com.example.javademo.dto.UpdateUserDTO;
import com.example.javademo.mapper.UserMapper;
import com.example.javademo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void update(UpdateUserDTO updateUserDTO) {
        userMapper.update(updateUserDTO);
    }
}
