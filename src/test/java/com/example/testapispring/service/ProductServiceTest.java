package com.example.testapispring.service;

import com.example.testapispring.model.Product;
import com.example.testapispring.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        Product expectedProduct = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        when(productRepository.findById(1L)).thenReturn(expectedProduct);

        // When
        Product actualProduct = productService.getProductById(1L);

        // Then
        assertEquals(expectedProduct, actualProduct);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNull() {
        // Given
        when(productRepository.findById(999L)).thenReturn(null);

        // When
        Product actualProduct = productService.getProductById(999L);

        // Then
        assertNull(actualProduct);
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        // Given
        Product product1 = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product product2 = new Product(2L, "Smartphone", "Latest model", 799.99, "Electronics", true);
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        
        when(productRepository.findByCategory("Electronics")).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.getProductsByCategory("Electronics");

        // Then
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByCategory("Electronics");
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
        verify(productRepository, times(1)).save(inputProduct);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
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
        assertEquals(expectedProduct.getId(), actualProduct.getId());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
        assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
        assertEquals(expectedProduct.getCategory(), actualProduct.getCategory());
        assertEquals(expectedProduct.getInStock(), actualProduct.getInStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNull() {
        // Given
        Product updatedDetails = new Product(null, "Updated Product", "New Description", 59.99, "Category", false);
        
        when(productRepository.findById(999L)).thenReturn(null);

        // When
        Product actualProduct = productService.updateProduct(999L, updatedDetails);

        // Then
        assertNull(actualProduct);
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnTrue() {
        // Given
        Product existingProduct = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        when(productRepository.findById(1L)).thenReturn(existingProduct);
        doNothing().when(productRepository).deleteById(anyLong());

        // When
        boolean result = productService.deleteProduct(1L);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnFalse() {
        // Given
        when(productRepository.findById(999L)).thenReturn(null);

        // When
        boolean result = productService.deleteProduct(999L);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }
} 