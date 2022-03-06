package com.example.jsonex.service;

import com.example.jsonex.model.dto.CategoriesByProductsCountDto;
import com.example.jsonex.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();

    List<CategoriesByProductsCountDto> findAllCategoriesOrderByNumberOfProducts();
}
