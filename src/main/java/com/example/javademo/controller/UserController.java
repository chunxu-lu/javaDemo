package com.example.javademo.controller;

import com.example.javademo.mapper.UserMapper;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.vo.UserlistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseResult<List<UserlistVo>>> getUsers() {
        return ResponseEntity.ok((new ResponseResult()).success(userMapper.selectUserList()));
    }
}
