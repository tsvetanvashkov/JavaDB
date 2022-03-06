package com.example.xmlex.service;

import com.example.xmlex.model.dto.CategorySeedDto;
import com.example.xmlex.model.entity.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories(List<CategorySeedDto> categories);

    long getEntityCount();

    Set<Category> getRandomCategories();
}
