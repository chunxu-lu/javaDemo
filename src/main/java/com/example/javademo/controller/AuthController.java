package com.example.javademo.controller;

import com.example.javademo.model.JwtResponse;
import com.example.javademo.model.LoginRequest;
import com.example.javademo.model.UserResponse;
import com.example.javademo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        if (!authenticationRequest.getUsername().equals("user") || !authenticationRequest.getPassword().equals("password")) {
            throw new Exception("Invalid credentials");
        }

        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7); // The part after "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);

        if (username != null && jwtUtil.validateToken(token, username)) {
            UserResponse response = new UserResponse(username);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }
}