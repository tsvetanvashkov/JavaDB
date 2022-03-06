package com.example.jsonex.service.impl;

import com.example.jsonex.model.dto.CategoriesByProductsCountDto;
import com.example.jsonex.model.dto.CategorySeedDto;
import com.example.jsonex.model.entity.Category;
import com.example.jsonex.repository.CategoryRepository;
import com.example.jsonex.service.CategoryService;
import com.example.jsonex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.jsonex.config.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_NAME = "categories.json";

    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0){
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos = gson
                .fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);

    }

    @Override
    public Set<Category> findRandomCategories() {
        Set<Category> categorySet = new HashSet<>();
        int catCount = ThreadLocalRandom.current().nextInt(1, 3);
        long totalCategoriesCount = categoryRepository.count();

        for (int i = 0; i < catCount; i++){
            long randomId = ThreadLocalRandom.current().nextLong(1,totalCategoriesCount + 1);
            categorySet.add(categoryRepository.findById(randomId).orElse(null));
        }
        return categorySet;
    }

    @Override
    public List<CategoriesByProductsCountDto> findAllCategoriesOrderByNumberOfProducts() {
        return categoryRepository
                .findAllByNameOrderByCountOfProducts()
                .stream()
                .map(category -> modelMapper.map(category, CategoriesByProductsCountDto.class))
                .collect(Collectors.toList());
    }
}
