package com.example.javademo.model;

import org.springframework.http.HttpStatus;

public class ResponseResult<T> {
    private int code;
    private String msg;
    private T data;

    // 默认构造函数、getter和setter方法
    public ResponseResult() {}

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    // getter and setter 方法
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // 快捷方法
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(HttpStatus.OK.value(), "success", data);
    }

    public static <T> ResponseResult<T> error(int code, String msg) {
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), msg, null);
    }
}