package com.example.xmlex;

import com.example.xmlex.model.dto.*;
import com.example.xmlex.service.CategoryService;
import com.example.xmlex.service.ProductService;
import com.example.xmlex.service.UserService;
import com.example.xmlex.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String RESOURCES_FILE_PATH = "src/main/resources/files/";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/files/out/";
    public static final String CATEGORY_FILE_NAME = "categories.xml";
    private static final  String USER_FILE_NAME = "users.xml";
    public static final String PRODUCT_FILE_NAME = "products.xml";
    public static final String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.xml";
    public static final String SOLD_PRODUCTS_FILE_NAME = "sold-products.xml";

    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(XmlParser xmlParser, CategoryService categoryService,
                                 UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Enter exercise:");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum){
            case 1 -> productsInRange();
            case 2 -> userWithSoldProducts();

        }

    }

    private void userWithSoldProducts() throws JAXBException {
        UserViewRootDto userViewRootDto = userService
                .findUsersWithMoreThanOneSoldProduct();
        xmlParser.writeToFile(OUTPUT_FILE_PATH + SOLD_PRODUCTS_FILE_NAME, userViewRootDto);
    }

    private void productsInRange() throws JAXBException {
        ProductViewRootDto productViewRootDto = productService
                .findProductsInRangeWithoutBuyer();
        xmlParser.writeToFile(OUTPUT_FILE_PATH + PRODUCT_IN_RANGE_FILE_NAME, productViewRootDto);
    }

    private void seedData() throws JAXBException, FileNotFoundException {
        if (categoryService.getEntityCount() == 0){
            CategorySeedRootDto categorySeedRootDto = xmlParser.fromFile(RESOURCES_FILE_PATH + CATEGORY_FILE_NAME,
                    CategorySeedRootDto.class);
            categoryService.seedCategories(categorySeedRootDto.getCategories());
        }
        if (userService.getEntityCount() == 0){
            UserSeedRootDto userSeedRootDto = xmlParser.fromFile(RESOURCES_FILE_PATH + USER_FILE_NAME,
                    UserSeedRootDto.class);
            userService.seedUsers(userSeedRootDto.getUsers());
        }
        if (productService.getEntityCount() == 0){
            ProductSeedRootDto productSeedRootDto = xmlParser.fromFile(RESOURCES_FILE_PATH + PRODUCT_FILE_NAME,
                    ProductSeedRootDto.class);
            productService.seedProducts(productSeedRootDto.getProducts());
        }


    }
}
