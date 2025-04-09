package com.example.javademo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductAddDTO {
    @NotNull(message = "不能为空")
    @NotEmpty(message = "不能为空")
    private String productName;
    @NotNull(message = "不能为空")
    @NotEmpty(message = "不能为空")
    private String category;
    @NotNull(message = "不能为空")
    @NotEmpty(message = "不能为空")
    private String supplier;
    @NotNull(message = "不能为空")
    private LocalDate productionDate;
    @NotNull(message = "不能为空")
    @NotEmpty(message = "不能为空")
    private String productCode;
    @NotNull(message = "不能为空")
    private Integer isActive;
    @NotNull(message = "不能为空")
    private Integer currentStock;
    @NotNull(message = "不能为空")
    private BigDecimal price;
}
