package com.example.javademo.controller;

import com.example.javademo.dto.UserLoginDTO;
import com.example.javademo.mapper.UserMapper;
import com.example.javademo.model.LoginRequest;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.model.JwtResponse;
import com.example.javademo.model.UserResponse;
import com.example.javademo.service.AuthService;
import com.example.javademo.service.RedisService;
import com.example.javademo.util.JwtUtil;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RedisService redisService;

    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserMapper userMapper, RedisService redisService, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        Optional<UserLoginDTO> userLoginDTO = userMapper.findByUsername(authenticationRequest.getUsername());
        if (userLoginDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "user not found", null));
        }

        String encryptedPassword = encryptPassword(authenticationRequest.getPassword());
        UserLoginDTO user = userLoginDTO.get(); // 解包 Optional
        if (!user.getPassword().equals(encryptedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "password not correct", null));
        }

        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername(), user.getId().toString()); // 传入用户ID
        redisService.saveData("userName:" + authenticationRequest.getUsername(), authenticationRequest.getUsername()); // 保存用户名到Redis
        redisService.saveData("session:" + jwt, authenticationRequest.getUsername()); // 保存用户名到Redis，键为session ID
        return ResponseEntity.ok(new ResponseResult().success(new JwtResponse(jwt)));
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
                    String token = authHeader.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            UserResponse userResponse = new UserResponse(username);
            return ResponseEntity.ok(new ResponseResult().success(userResponse));
    }

    @PostMapping("/clear-chat-history")
    public ResponseEntity<?> clearChatHistory() {
        redisService.clearChatHistory();
        return ResponseEntity.ok(new ResponseResult().success("Chat history cleared successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null));
        }

        String token = authHeader.substring(7); // The part after "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if (username != null) {
            redisService.deleteData("userName:" + username); // 删除用户名
            redisService.deleteData("session:" + token); // 删除会话ID
            return ResponseEntity.ok(new ResponseResult().success("Logged out successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseResult(HttpStatus.FORBIDDEN.value(), "Forbidden", null));
        }
    }

    @GetMapping("/path")
    public ResponseEntity<?> getPath(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // The part after "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);
        Object paths = authService.getPath(username);
        return ResponseEntity.ok(new ResponseResult().success(paths));
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
