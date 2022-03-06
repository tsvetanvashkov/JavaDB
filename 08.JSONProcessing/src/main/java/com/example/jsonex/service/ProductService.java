package com.example.jsonex.service;

import com.example.jsonex.model.dto.ProductNameAndPriceDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProduct() throws IOException;

    List<ProductNameAndPriceDto> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper);
}
