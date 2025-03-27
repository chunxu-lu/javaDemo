package com.example.javademo.config;

import com.example.javademo.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// 配置类，启用Web安全
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // 定义SecurityFilterChain Bean，配置HTTP安全设置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 明确禁用CSRF
                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        // 允许所有用户访问登录接口
                        .requestMatchers("/api/auth/login").permitAll()
                        // 允许所有用户访问/api/captcha/**路径下的资源
                        .requestMatchers("/api/captcha/**").permitAll()
                        // 其他所有请求需要验证token
                        .anyRequest().authenticated()
                )
                // 添加JWT过滤器到安全过滤链中
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置跨域资源共享
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // 返回构建好的SecurityFilterChain
        return http.build();
    }

    // 定义CorsConfigurationSource Bean，配置跨域资源共享
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 允许所有来源，生产环境需指定域名
        configuration.addAllowedMethod("*"); // 允许所有HTTP方法
        configuration.addAllowedHeader("*"); // 允许所有请求头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}