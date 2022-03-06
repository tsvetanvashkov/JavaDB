package com.example.jsonex;

import com.example.jsonex.model.dto.CategoriesByProductsCountDto;
import com.example.jsonex.model.dto.ProductNameAndPriceDto;
import com.example.jsonex.model.dto.UserSoldDto;
import com.example.jsonex.service.CategoryService;
import com.example.jsonex.service.ProductService;
import com.example.jsonex.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static com.example.jsonex.config.constants.GlobalConstants.OUTPUT_FILE_PATH;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final static String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.json";
    private final static String USERS_AND_SOLD_PRODUCTS_FILE_NAME = "users-and-sold-products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Enter exercise:");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum){
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            //case 3 -> categoriesByProductsCount();
        }


    }

    private void categoriesByProductsCount() {
        List<CategoriesByProductsCountDto> categoriesByProductsCountDtos = categoryService
                .findAllCategoriesOrderByNumberOfProducts();

        String content = gson.toJson(categoriesByProductsCountDtos);


    }

    private void soldProducts() throws IOException {
        List<UserSoldDto> userSoldDtos = userService.findAllUsersWithMoreThanOneSoldProducts();

        String content = gson.toJson(userSoldDtos);

        writeToFile(OUTPUT_FILE_PATH + USERS_AND_SOLD_PRODUCTS_FILE_NAME, content);

    }

    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDto> productDtos = productService
                .findAllProductsInRangeOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productDtos);
        writeToFile(OUTPUT_FILE_PATH + PRODUCT_IN_RANGE_FILE_NAME, content);

    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));


    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProduct();
    }
}
