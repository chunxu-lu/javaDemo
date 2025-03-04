package com.example.javademo.model; // 确保这里的包声明是正确的

public class JwtResponse {
    private String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    // Getter for jwtToken
    public String getJwtToken() {
        return jwtToken;
    }

    // Setter for jwtToken
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}