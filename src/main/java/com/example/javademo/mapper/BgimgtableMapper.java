package com.example.javademo.mapper;

import com.example.javademo.entity.Bgimgtable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BgimgtableMapper {
    List<Bgimgtable> selectAll();

    void insert(Bgimgtable bgimgtable);

    Bgimgtable selectById(@Param("id") Long id);

    void deleteById(@Param("id") Long id);

    void resetAllFlags();
}