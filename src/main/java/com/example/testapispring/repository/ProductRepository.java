package com.example.testapispring.repository;

import com.example.testapispring.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @PostConstruct
    public void initProducts() {
        // Sample products
        addSampleProduct("Laptop", "High-performance laptop with SSD", 1299.99, "Electronics", true);
        addSampleProduct("Smartphone", "Latest model with advanced camera", 799.99, "Electronics", true);
        addSampleProduct("Headphones", "Noise-cancelling wireless headphones", 199.99, "Electronics", true);
        addSampleProduct("Office Chair", "Ergonomic chair for home office", 299.99, "Furniture", false);
        addSampleProduct("Coffee Maker", "Automatic coffee machine with timer", 89.99, "Appliances", true);
    }

    private void addSampleProduct(String name, String description, double price, String category, boolean inStock) {
        Product product = new Product();
        product.setId(idCounter.incrementAndGet());
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setInStock(inStock);
        products.put(product.getId(), product);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Product findById(Long id) {
        return products.get(id);
    }

    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(product -> category.equals(product.getCategory()))
                .toList();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idCounter.incrementAndGet());
        }
        products.put(product.getId(), product);
        return product;
    }

    public void deleteById(Long id) {
        products.remove(id);
    }
} 