package com.example.javademo.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> list;  // 当前页数据
    private long total;    // 总记录数

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }
}
