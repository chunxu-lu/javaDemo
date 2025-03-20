package com.example.javademo.controller;

import com.example.javademo.dto.UserLoginDTO;
import com.example.javademo.mapper.UserMapper;
import com.example.javademo.model.JwtResponse;
import com.example.javademo.model.LoginRequest;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.model.UserResponse;
import com.example.javademo.service.RedisService;
import com.example.javademo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    private final RedisService redisService; // 添加RedisService

    @Autowired
    public AuthController(JwtUtil jwtUtil,RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        UserLoginDTO userLoginDTO = userMapper.findByUsername(authenticationRequest.getUsername());
        if(userLoginDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"user not found",null));
        }

        String encryptedPassword = encryptPassword(authenticationRequest.getPassword());
        if(!userLoginDTO.getPassword().equals(encryptedPassword)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"password not correct",null));
        }

        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());
        redisService.saveData("userName", authenticationRequest.getUsername());
        return ResponseEntity.ok(new ResponseResult().success(new JwtResponse(jwt)));
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null));
        }

        String token = authHeader.substring(7); // The part after "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if (username != null && jwtUtil.validateToken(token, username)) {
            UserResponse response = new UserResponse(username);
            return ResponseEntity.ok(new ResponseResult().success(response));
        } else {
            return ResponseEntity.ok(new ResponseResult(HttpStatus.FORBIDDEN.value(), "Forbidden", null));
        }
    }

    // 新增方法：SHA256 加密
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
