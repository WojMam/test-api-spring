package com.example.testapispring.service;

import com.example.testapispring.exception.ResourceNotFoundException;
import com.example.testapispring.model.Product;
import com.example.testapispring.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        Product product1 = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product product2 = new Product(2L, "Smartphone", "Latest model", 799.99, "Electronics", true);
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.getAllProducts();

        // Then
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Given
        Product expectedProduct = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        when(productRepository.findById(1L)).thenReturn(expectedProduct);

        // When
        Product actualProduct = productService.getProductById(1L);

        // Then
        assertEquals(expectedProduct, actualProduct);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(productRepository.findById(999L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(999L);
        });
        
        verify(productRepository).findById(999L);
    }

    @Test
    void createProduct_ShouldReturnSavedProduct() {
        // Given
        Product inputProduct = new Product(null, "New Product", "Description", 49.99, "Category", true);
        Product savedProduct = new Product(3L, "New Product", "Description", 49.99, "Category", true);
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product actualProduct = productService.createProduct(inputProduct);

        // Then
        assertEquals(savedProduct, actualProduct);
        verify(productRepository).save(inputProduct);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() {
        // Given
        Product existingProduct = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product updatedDetails = new Product(null, "Updated Laptop", "New Description", 1499.99, "Electronics", false);
        Product expectedProduct = new Product(1L, "Updated Laptop", "New Description", 1499.99, "Electronics", false);
        
        when(productRepository.findById(1L)).thenReturn(existingProduct);
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // When
        Product actualProduct = productService.updateProduct(1L, updatedDetails);

        // Then
        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Product updatedDetails = new Product(999L, "Updated Laptop", "Updated description", 1499.99, "Electronics", true);
        when(productRepository.findById(999L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(999L, updatedDetails);
        });
        
        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteSuccessfully() {
        // Given
        Product existingProduct = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        when(productRepository.findById(1L)).thenReturn(existingProduct);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        when(productRepository.findById(999L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(999L);
        });
        
        verify(productRepository).findById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }
} 