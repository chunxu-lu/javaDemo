package com.example.javademo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key"; // 更换为一个安全的密钥
    private static final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // Token有效期5小时

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));
    }

    public boolean validateToken(String token, String username) {
        try {
            String subject = JWT.require(Algorithm.HMAC256(SECRET_KEY.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            return subject.equals(username);
        } catch (JWTVerificationException exception){
            // Token is invalid or expired
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            // Token verification failed
            return null;
        }
    }
}