package com.example.javademo.config;

import com.example.javademo.filter.JwtRequestFilter;
import com.example.javademo.security.CustomAuthenticationEntryPoint;
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
import com.example.javademo.security.CustomAccessDeniedHandler;

// 配置类，启用Web安全
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;

    // 定义SecurityFilterChain Bean，配置HTTP安全设置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler) // 注入自定义处理器
                )
                .csrf(csrf -> csrf.disable()) // 明确禁用CSRF
                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/chat", "/sockjs-node/**").permitAll() // 放行 WebSocket 和 SockJS 路径
                        // 允许所有用户访问登录接口
                        .requestMatchers("/api/auth/login").permitAll()
                        // 允许所有用户访问/api/captcha/**路径下的资源
                        .requestMatchers("/api/captcha/**").permitAll()
                        // 仅允许管理员角色访问/api/admin/**路径下的资源
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
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