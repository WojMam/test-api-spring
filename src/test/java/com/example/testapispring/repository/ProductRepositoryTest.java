package com.example.testapispring.repository;

import com.example.testapispring.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Create a fresh repository instance for each test
        productRepository = new ProductRepository();
        productRepository.initProducts();
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // When
        List<Product> products = productRepository.findAll();
        
        // Then
        assertEquals(5, products.size());
    }

    @Test
    void findById_ShouldReturnProductWhenExists() {
        // Given
        List<Product> products = productRepository.findAll();
        Long id = products.get(0).getId();
        
        // When
        Product foundProduct = productRepository.findById(id);
        
        // Then
        assertNotNull(foundProduct);
        assertEquals(id, foundProduct.getId());
    }

    @Test
    void findById_ShouldReturnNullWhenDoesNotExist() {
        // When
        Product foundProduct = productRepository.findById(999L);
        
        // Then
        assertNull(foundProduct);
    }

    @Test
    void findByCategory_ShouldReturnProductsInCategory() {
        // When
        List<Product> electronicsProducts = productRepository.findByCategory("Electronics");
        
        // Then
        assertFalse(electronicsProducts.isEmpty());
        assertTrue(electronicsProducts.stream()
                .allMatch(p -> p.getCategory().equals("Electronics")));
    }

    @Test
    void findByCategory_ShouldReturnEmptyListWhenCategoryDoesNotExist() {
        // When
        List<Product> products = productRepository.findByCategory("NonExistentCategory");
        
        // Then
        assertTrue(products.isEmpty());
    }

    @Test
    void save_ShouldAddNewProduct() {
        // Given
        int initialSize = productRepository.findAll().size();
        Product newProduct = new Product(null, "Test Product", "For testing", 9.99, "Test", true);
        
        // When
        Product savedProduct = productRepository.save(newProduct);
        
        // Then
        assertNotNull(savedProduct.getId());
        assertEquals(initialSize + 1, productRepository.findAll().size());
        
        // Verify we can find it by ID
        Product foundProduct = productRepository.findById(savedProduct.getId());
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void save_ShouldUpdateExistingProduct() {
        // Given
        Product newProduct = new Product(null, "Product to Update", "Original description", 19.99, "Test", true);
        Product savedProduct = productRepository.save(newProduct);
        Long id = savedProduct.getId();
        
        // Update the product
        savedProduct.setName("Updated Name");
        savedProduct.setPrice(29.99);
        
        // When
        productRepository.save(savedProduct);
        
        // Then
        Product foundProduct = productRepository.findById(id);
        assertEquals("Updated Name", foundProduct.getName());
        assertEquals(29.99, foundProduct.getPrice());
    }

    @Test
    void deleteById_ShouldRemoveProduct() {
        // Given
        List<Product> initialProducts = productRepository.findAll();
        Long id = initialProducts.get(0).getId();
        int initialSize = initialProducts.size();
        
        // When
        productRepository.deleteById(id);
        
        // Then
        assertNull(productRepository.findById(id));
        assertEquals(initialSize - 1, productRepository.findAll().size());
    }
} 