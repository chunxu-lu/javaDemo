package com.example.javademo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    String getPath(String username);
}
