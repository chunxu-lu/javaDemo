package com.example.javademo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MenuDTO {
    @JsonProperty("menu")  // 映射JSON中的"menu"字段
    private List<MenuGroup> menuGroups;  // 系统管理、用户管理等分组

    // Getter & Setter
    public List<MenuGroup> getMenuGroups() {
        return menuGroups;
    }

    public void setMenuGroups(List<MenuGroup> menuGroups) {
        this.menuGroups = menuGroups;
    }
}
