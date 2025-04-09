package com.example.javademo.controller;

import com.example.javademo.common.PageResult;
import com.example.javademo.dto.ProductAddDTO;
import com.example.javademo.dto.ProductQueryDTO;
import com.example.javademo.entity.Product;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<ResponseResult<PageResult<Product>>> productList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, ProductQueryDTO productQueryDTO) {
        PageResult<Product> products = productService.getProductList(page, size, productQueryDTO);
        return ResponseEntity.ok(ResponseResult.success(products));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseResult> addProduct(@RequestBody @Valid ProductAddDTO productAddDTO) {
        productService.addProduct(productAddDTO);
        return ResponseEntity.ok(ResponseResult.success(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseResult> deleteProduct(@RequestBody Map<String, String> requestMap) {
        String ids = requestMap.get("ids");
        System.out.println("VVV"+ids);
        // 将逗号分隔的字符串转换为List
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        // 调用服务层执行删除
        productService.deleteProducts(idList);
        return ResponseEntity.ok(ResponseResult.success(null));
    }
}
