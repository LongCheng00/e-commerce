package com.example.demo.test.service;

import com.example.demo.model.Product;
import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        // When
        List<Product> products = productService.getProducts();
        
        // Then
        assertEquals(3, products.size());
        assertEquals("Product 1", products.get(0).name());
    }

    @Test
    void getProductById_shouldReturnProductWhenExists() {
        // When
        Optional<Product> product = productService.getProductById(1L);
        
        // Then
        assertTrue(product.isPresent());
        assertEquals("Product 1", product.get().name());
    }

    @Test
    void getProductById_shouldReturnEmptyWhenNotExists() {
        // When
        Optional<Product> product = productService.getProductById(99L);
        
        // Then
        assertTrue(product.isEmpty());
    }

    @Test
    void checkStock_shouldReturnTrueWhenSufficientStock() {
        // When
        boolean result = productService.checkStock(1L, 5);
        
        // Then
        assertTrue(result);
    }

    @Test
    void checkStock_shouldReturnFalseWhenInsufficientStock() {
        // When
        boolean result = productService.checkStock(1L, 20);
        
        // Then
        assertFalse(result);
    }

    @Test
    void checkStock_shouldReturnFalseForNonexistentProduct() {
        // When
        boolean result = productService.checkStock(99L, 1);
        
        // Then
        assertFalse(result);
    }

    @Test
    void reduceStock_shouldSucceedWhenSufficientStock() {
        // Given
        int initialStock = productService.getProductById(1L).get().stock();
        
        // When
        boolean result = productService.reduceStock(1L, 5);
        
        // Then
        assertTrue(result);
        assertEquals(initialStock - 5, productService.getProductById(1L).get().stock());
    }

    @Test
    void reduceStock_shouldFailWhenInsufficientStock() {
        // Given
        int initialStock = productService.getProductById(1L).get().stock();
        
        // When
        boolean result = productService.reduceStock(1L, 20);
        
        // Then
        assertFalse(result);
        assertEquals(initialStock, productService.getProductById(1L).get().stock());
    }

    @Test
    void reduceStock_shouldFailForNonexistentProduct() {
        // When
        boolean result = productService.reduceStock(99L, 1);
        
        // Then
        assertFalse(result);
    }
}