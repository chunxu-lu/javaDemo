package com.example.javademo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class ProductQueryDTO {
    private String productCode;

    private String productName;

    private String category;

    private String supplier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate productionDate;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdatedStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdatedEnd;

    public LocalDate getLastUpdatedStart() {
        return lastUpdatedStart;
    }

    public void setLastUpdatedStart(LocalDate lastUpdatedStart) {
        this.lastUpdatedStart = lastUpdatedStart;
    }

    public LocalDate getLastUpdatedEnd() {
        return lastUpdatedEnd;
    }

    public void setLastUpdatedEnd(LocalDate lastUpdatedEnd) {
        this.lastUpdatedEnd = lastUpdatedEnd;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
