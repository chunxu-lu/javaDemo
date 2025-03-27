package com.example.javademo.entity;
import java.util.Date;

public class User {
    private Long id;
    private String user_name;
    private String password;
    private String nick_name;
    private String name;
    private int age;
    private Long card_number;
    private Date registration_time;
    private String email;
    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nick_name;
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getCardNumber() {
        return card_number;
    }

    public void setCardNumber(Long card_number) {
        this.card_number = card_number;
    }

    public Date getRegistrationTime() {
        return registration_time;
    }

    public void setRegistrationTime(Date registration_time) {
        this.registration_time = registration_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
