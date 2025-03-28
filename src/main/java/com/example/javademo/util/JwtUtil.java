package com.example.javademo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TOKEN_VALIDITY = 30 * 1000; // 30 seconds

    /**
     * 生成 Token（仅负责生成，不验证）
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 从 Token 中解析用户名（不验证有效性，仅解析）
     * 注意：调用方需确保 Token 已通过 Spring Security 验证！
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 获取密钥（供 Spring Security 配置使用）
     */
    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }

    /**
     * 验证 Token 是否有效（仅解析，不抛出异常）
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true; // Token 有效
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            throw e;
        }
    }
}