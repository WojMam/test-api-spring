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
        productRepository = new ProductRepository();
        // The @PostConstruct init method is not automatically called in tests
        // So we need to explicitly invoke it
        productRepository.init();
    }

    @Test
    void init_ShouldInitializeWithSampleProducts() {
        // When
        List<Product> products = productRepository.findAll();
        
        // Then
        assertEquals(5, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Smartphone")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Headphones")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Office Chair")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Coffee Maker")));
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // When
        List<Product> products = productRepository.findAll();
        
        // Then
        assertEquals(5, products.size());
    }

    @Test
    void findById_WhenProductExists_ShouldReturnProduct() {
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
    void findById_WhenProductDoesNotExist_ShouldReturnNull() {
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
    void findByCategory_WithNonExistentCategory_ShouldReturnEmptyList() {
        // When
        List<Product> products = productRepository.findByCategory("NonExistentCategory");
        
        // Then
        assertTrue(products.isEmpty());
    }

    @Test
    void save_NewProduct_ShouldAssignIdAndAddToRepository() {
        // Given
        Product newProduct = new Product(null, "Test Product", "For testing", 9.99, "Test", true);
        
        // When
        Product savedProduct = productRepository.save(newProduct);
        
        // Then
        assertNotNull(savedProduct.getId());
        Product foundProduct = productRepository.findById(savedProduct.getId());
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
        assertEquals("For testing", foundProduct.getDescription());
        assertEquals(9.99, foundProduct.getPrice());
        assertEquals("Test", foundProduct.getCategory());
        assertTrue(foundProduct.getInStock());
    }

    @Test
    void save_ExistingProduct_ShouldUpdateAndReturnProduct() {
        // Given
        List<Product> products = productRepository.findAll();
        Product existingProduct = products.get(0);
        Long id = existingProduct.getId();
        
        existingProduct.setName("Updated Name");
        existingProduct.setDescription("Updated Description");
        existingProduct.setPrice(99.99);
        existingProduct.setCategory("Updated Category");
        existingProduct.setInStock(false);
        
        // When
        Product updatedProduct = productRepository.save(existingProduct);
        
        // Then
        assertEquals(id, updatedProduct.getId()); // ID should remain the same
        Product foundProduct = productRepository.findById(id);
        assertEquals("Updated Name", foundProduct.getName());
        assertEquals("Updated Description", foundProduct.getDescription());
        assertEquals(99.99, foundProduct.getPrice());
        assertEquals("Updated Category", foundProduct.getCategory());
        assertFalse(foundProduct.getInStock());
    }

    @Test
    void deleteById_WhenProductExists_ShouldRemoveProduct() {
        // Given
        List<Product> products = productRepository.findAll();
        int initialSize = products.size();
        Long id = products.get(0).getId();
        
        // When
        productRepository.deleteById(id);
        
        // Then
        assertNull(productRepository.findById(id));
        assertEquals(initialSize - 1, productRepository.findAll().size());
    }

    @Test
    void deleteById_WhenProductDoesNotExist_ShouldNotChangeRepository() {
        // Given
        int initialSize = productRepository.findAll().size();
        
        // When
        productRepository.deleteById(999L);
        
        // Then
        assertEquals(initialSize, productRepository.findAll().size());
    }
} 