package com.example.jsonex.service.impl;

import com.example.jsonex.model.dto.ProductNameAndPriceDto;
import com.example.jsonex.model.dto.ProductSeedDto;
import com.example.jsonex.model.entity.Product;
import com.example.jsonex.repository.ProductRepository;
import com.example.jsonex.service.CategoryService;
import com.example.jsonex.service.ProductService;
import com.example.jsonex.service.UserService;
import com.example.jsonex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.jsonex.config.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private final static String PRODUCTS_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedProduct() throws IOException {
        if(productRepository.count() > 0){
            return;
        }
        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.findRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0){
                        product.setBuyer(userService.findRandomUser());
                    }
                    product.setCategories(categoryService.findRandomCategories());
                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDto> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceDesc(lower,upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDto productNameAndPriceDto = modelMapper
                            .map(product, ProductNameAndPriceDto.class);
                    productNameAndPriceDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));
                    return productNameAndPriceDto;
                })
                .collect(Collectors.toList());
    }
}
