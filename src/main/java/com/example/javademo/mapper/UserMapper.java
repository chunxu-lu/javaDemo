package com.example.javademo.mapper;

import com.example.javademo.dto.UpdateUserDTO;
import com.example.javademo.dto.UserLoginDTO;
import com.example.javademo.entity.User;
import com.example.javademo.enums.UserRole;
import com.example.javademo.vo.UserlistVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<User> selectAll();

    Optional<UserLoginDTO> findByUsername(@Param("username") String username);

    List<UserlistVo> selectUserList();

    UserRole getUserRole(String username);

    void update(UpdateUserDTO updateUserDTO);
}