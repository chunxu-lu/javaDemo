package com.example.javademo.dto;

public class MenuItem {
    private String path;   // 如"/user"
    private String title;  // 如"用户管理"

    // Getter & Setter
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

