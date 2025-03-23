package com.example.testapispring.controller;

import com.example.testapispring.model.Product;
import com.example.testapispring.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PublicProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;
    
    @InjectMocks
    private PublicProductController controller;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        Product product1 = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product product2 = new Product(2L, "Smartphone", "Latest model", 799.99, "Electronics", true);
        List<Product> products = Arrays.asList(product1, product2);
        
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/public/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Smartphone")));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Given
        Product product = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        when(productService.getProductById(1L)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/public/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(1299.99)));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.getProductById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/public/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsByCategory() throws Exception {
        // Given
        Product product1 = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product product2 = new Product(2L, "Smartphone", "Latest model", 799.99, "Electronics", true);
        List<Product> products = Arrays.asList(product1, product2);
        
        when(productService.getProductsByCategory("Electronics")).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/public/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Smartphone")));
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        Product inputProduct = new Product(null, "New Product", "Description", 49.99, "Category", true);
        Product createdProduct = new Product(3L, "New Product", "Description", 49.99, "Category", true);
        
        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        // When & Then
        mockMvc.perform(post("/public/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Product")));
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        Product inputProduct = new Product(null, "Updated Product", "New Description", 59.99, "Category", false);
        Product updatedProduct = new Product(1L, "Updated Product", "New Description", 59.99, "Category", false);
        
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        // When & Then
        mockMvc.perform(put("/public/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(59.99)));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        Product inputProduct = new Product(null, "Updated Product", "New Description", 59.99, "Category", false);
        
        when(productService.updateProduct(eq(999L), any(Product.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/public/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputProduct)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(productService.deleteProduct(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/public/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.deleteProduct(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/public/products/999"))
                .andExpect(status().isNotFound());
    }
} 