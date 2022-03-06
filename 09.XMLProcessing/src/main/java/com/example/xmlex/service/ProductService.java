package com.example.xmlex.service;

import com.example.xmlex.model.dto.ProductSeedDto;
import com.example.xmlex.model.dto.ProductViewRootDto;

import java.util.List;

public interface ProductService {
    long getEntityCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findProductsInRangeWithoutBuyer();
}
