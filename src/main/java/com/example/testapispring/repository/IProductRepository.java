package com.example.testapispring.repository;

import com.example.testapispring.model.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> findByCategory(String category);
    Product save(Product product);
    void deleteById(Long id);
} 