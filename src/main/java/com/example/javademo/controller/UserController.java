package com.example.javademo.controller;

import com.example.javademo.entity.User;
import com.example.javademo.mapper.UserMapper;
import com.example.javademo.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @GetMapping("/list")
    public ResponseResult<List<User>>  getUsers(){
        return new ResponseResult(200,"success",userMapper.selectAll());
    }
}
