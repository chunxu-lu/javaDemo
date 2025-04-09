package com.example.javademo.filter;

import com.example.javademo.exceptions.CustomAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.javademo.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // 定义无需认证的接口路径
    private static final String[] WHITE_LIST = {
            "/api/captcha/get",  // 验证码接口
            "/api/captcha/check",  // 验证码接口
            "/api/auth/login",    // 登录接口
    };

    private boolean isWhiteListRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(WHITE_LIST).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        String username = null;
        String jwt = null;
        // 获取请求路径
        String path = request.getRequestURI();

        System.out.println("path   " + path);

        // 检查是否是白名单路径
        if (isWhiteListRequest(request)) {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);


                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 如果是 WebSocket 相关路径
            if (path.startsWith("/chat")) {
                jwt = request.getParameter("jwt");

                System.out.println("jwt   " + jwt);
                username = jwtUtil.getUsernameFromToken(jwt);

            } else {
                final String authorizationHeader = request.getHeader("Authorization");


                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    jwt = authorizationHeader.substring(7);
                    System.out.println("----------------------------------------");
                }

                log.info("tttttttttttttttttttttttttttttttttttttttt");
                System.out.println(jwt);

                // 解析 Token 获取用户名
                username = jwtUtil.getUsernameFromToken(jwt);
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("ex", new CustomAuthenticationException("token已过期,请重新登录", e));
        } catch (MalformedJwtException e) {
            request.setAttribute("ex", new CustomAuthenticationException("token格式无效", e));
        } catch (IllegalArgumentException e) {
            request.setAttribute("ex", new CustomAuthenticationException("请求头缺失token", e));
        } catch (Exception e){
            request.setAttribute("ex", new CustomAuthenticationException(e.getMessage(), e));
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);


            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        filterChain.doFilter(request, response);
    }
}