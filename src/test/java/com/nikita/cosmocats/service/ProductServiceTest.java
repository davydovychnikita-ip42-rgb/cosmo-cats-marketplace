package com.nikita.cosmocats.service;

import com.nikita.cosmocats.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void createProductShouldSaveAndReturnProduct() {
        Product product = new Product();
        product.setName("STAR CATNIP");
        product.setPrice(new BigDecimal("19.99"));

        Product saved = productService.createProduct(product);

        assertNotNull(saved.getId());
        assertEquals("STAR CATNIP", saved.getName());
        assertEquals(new BigDecimal("19.99"), saved.getPrice());
        assertEquals(1L, productService.count());
    }

    @Test
    void getAllProductsShouldReturnPagedResults() {
        for (int i = 1; i <= 5; i++) {
            Product product = new Product();
            product.setName("GALAXY TOY " + i);
            product.setPrice(BigDecimal.valueOf(10L * i));
            productService.createProduct(product);
        }

        List<Product> firstPage = productService.getAllProducts(0, 2);
        List<Product> secondPage = productService.getAllProducts(1, 2);
        List<Product> lastPage = productService.getAllProducts(2, 2);
        List<Product> emptyPage = productService.getAllProducts(5, 2);

        assertEquals(2, firstPage.size());
        assertEquals(2, secondPage.size());
        assertEquals(1, lastPage.size());
        assertTrue(emptyPage.isEmpty());
    }

    @Test
    void countShouldReturnZeroWhenDatabaseIsEmpty() {
        assertEquals(0L, productService.count());
    }
}