package com.example.javademo.dto;

import java.util.List;

public class MenuGroup {
    private String title;          // 如"系统管理"
    private List<MenuItem> children; // 子菜单项

    // Getter & Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }
}
