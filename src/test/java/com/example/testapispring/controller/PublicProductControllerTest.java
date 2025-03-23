package com.example.testapispring.controller;

import com.example.testapispring.model.Product;
import com.example.testapispring.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicProductController.class)
class PublicProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private com.example.testapispring.security.JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private com.example.testapispring.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        Product product1 = new Product(1L, "Laptop", "High-performance laptop", 1299.99, "Electronics", true);
        Product product2 = new Product(2L, "Smartphone", "Latest model", 799.99, "Electronics", true);
        List<Product> products = Arrays.asList(product1, product2);
        
        given(productService.getAllProducts()).willReturn(products);

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
        given(productService.getProductById(1L)).willReturn(product);

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
        given(productService.getProductById(999L)).willReturn(null);

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
        
        given(productService.getProductsByCategory("Electronics")).willReturn(products);

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
        
        given(productService.createProduct(any(Product.class))).willReturn(createdProduct);

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
        
        given(productService.updateProduct(eq(1L), any(Product.class))).willReturn(updatedProduct);

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
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() throws Exception {
        // Given
        given(productService.deleteProduct(1L)).willReturn(true);

        // When & Then
        mockMvc.perform(delete("/public/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        given(productService.deleteProduct(999L)).willReturn(false);

        // When & Then
        mockMvc.perform(delete("/public/products/999"))
                .andExpect(status().isNotFound());
    }
} 