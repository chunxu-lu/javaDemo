package com.example.javademo.mapper;

import com.example.javademo.dto.UserLoginDTO;
import com.example.javademo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll();

    UserLoginDTO findByUsername(@Param("username") String username);
}