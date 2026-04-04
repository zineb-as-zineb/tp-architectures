package com.ecommerce.monolith.repository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.repository.CategoryRepository;
import com.ecommerce.monolith.product.model.Product;
import com.ecommerce.monolith.product.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class RestRepositoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = categoryRepository.save(Category.builder()
                .name("Electronics")
                .build());

        productRepository.save(Product.builder()
                .name("Laptop")
                .description("Portable computer")
                .price(BigDecimal.valueOf(999.99))
                .stock(10)
                .category(category)
                .build());
    }

    @Test
    void shouldExposeCategoriesRestRepository() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories[0].name").value("Electronics"));
    }

    @Test
    void shouldExposeProductsRestRepositoryWithCategoryRelation() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products[0].name").value("Laptop"))
                .andExpect(jsonPath("$._embedded.products[0]._links.category.href").exists());
    }

    @Test
    void shouldSearchProductsByNameThroughRestRepository() throws Exception {
        mockMvc.perform(get("/products/search/by-name").param("name", "lap"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products[0].name").value("Laptop"));
    }
}
