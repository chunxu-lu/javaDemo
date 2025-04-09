package com.example.javademo.service.impl;

import com.example.javademo.common.PageResult;
import com.example.javademo.dto.ProductAddDTO;
import com.example.javademo.dto.ProductQueryDTO;
import com.example.javademo.entity.Product;
import com.example.javademo.mapper.ProductMapper;
import com.example.javademo.service.ProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageResult<Product> getProductList(int page, int size, ProductQueryDTO productQueryDTO) {
        log.info(productQueryDTO.getProductCode());
        // 只需这一行代码即可触发分页
        PageHelper.startPage(page, size);
        // 2. 执行查询（返回的是 List，但底层是 Page 对象）
        List<Product> productList = productMapper.getProductList(productQueryDTO);

        // 3. 获取总记录数（PageHelper 内部实现）
        long total = ((Page) productList).getTotal();
        return new PageResult(productList, total);
    }

    @Override
    public void addProduct(ProductAddDTO productAddDTO){
        productMapper.addProduct(productAddDTO);
    }

    @Override
    public void deleteProducts(List<Long> idList) {
        productMapper.deleteProducts(idList);
    }
}
