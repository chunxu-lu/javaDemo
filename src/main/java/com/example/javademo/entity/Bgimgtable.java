package com.example.javademo.entity;

public class Bgimgtable {
    private Long id;
    private String img_url;
    private String img_name;
    private int flag; // 新增flag字段

    public Bgimgtable() { // 修改构造函数
    }

    public Bgimgtable(Long id, String img_url, String img_name,int flag) { // 修改构造函数
        this.id = id;
        this.img_url = img_url;
        this.img_name = img_name;
        this.flag = flag;
    }

    public String getImgName() {
        return img_name;
    }

    public void setImgName(String img_name) {
        this.img_name = img_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}