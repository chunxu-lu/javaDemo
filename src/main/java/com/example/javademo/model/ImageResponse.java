package com.example.javademo.model;

public class ImageResponse {
    private Long id;
    private String imgUrl;
    private String imgName; // 添加 imgName 字段

    public ImageResponse(Long id, String imgUrl, String imgName) { // 修改构造函数
        this.id = id;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() { // 添加 getter 方法
        return imgName;
    }

    public void setImgName(String imgName) { // 添加 setter 方法
        this.imgName = imgName;
    }
}