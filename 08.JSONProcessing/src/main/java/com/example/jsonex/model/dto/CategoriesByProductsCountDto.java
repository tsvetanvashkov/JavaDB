package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class CategoriesByProductsCountDto {

    @Expose
    private String name;
    @Expose
    private Integer numberOfProducts;
    @Expose
    private BigDecimal averagePrice;
    @Expose
    private BigDecimal totalRevenue;

    public CategoriesByProductsCountDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
