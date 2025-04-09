package com.example.javademo.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.javademo.exceptions.CustomAuthenticationException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        // 获取自定义异常
        CustomAuthenticationException customException =
                (CustomAuthenticationException) request.getAttribute("ex");

        response.setContentType("application/json;charset=UTF-8");

        if (customException != null) {
            String  errorMessage = customException.getMessage();
            // 返回 JSON 格式的响应
            response.getWriter().write(
                    String.format("{\"code\": 401, \"error\": \"unauthorized\", \"msg\": \"%s\"}", errorMessage)
            );
        }
    }
}
