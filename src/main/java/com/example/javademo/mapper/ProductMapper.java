package com.example.javademo.mapper;

import com.example.javademo.dto.ProductAddDTO;
import com.example.javademo.dto.ProductQueryDTO;
import com.example.javademo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getProductList(ProductQueryDTO productQueryDTO);

    void addProduct(ProductAddDTO productAddDTO);

    void deleteProducts(List<Long> idList);
}
