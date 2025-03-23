package com.example.testapispring.service;

import com.example.testapispring.model.Product;
import com.example.testapispring.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setInStock(productDetails.getInStock());

        return productRepository.save(existingProduct);
    }

    public boolean deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct == null) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
} 