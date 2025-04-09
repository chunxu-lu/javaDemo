package com.example.javademo.service;

import com.example.javademo.common.PageResult;
import com.example.javademo.dto.ProductAddDTO;
import com.example.javademo.dto.ProductQueryDTO;
import com.example.javademo.entity.Product;

import java.util.List;

public interface ProductService {
    PageResult<Product> getProductList(int page, int size, ProductQueryDTO productQueryDTO);

    void addProduct(ProductAddDTO productAddDTO);

    void deleteProducts(List<Long> idList);
}
