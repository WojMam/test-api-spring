package com.example.testapispring.model;

import com.example.testapispring.validation.ValidationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

public class Product {
    private Long id;
    
    @NotBlank(message = "Nazwa produktu jest wymagana", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 2, max = 100, message = "Nazwa produktu musi mieć od 2 do 100 znaków", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;
    
    @Size(max = 500, message = "Opis nie może przekraczać 500 znaków", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String description;
    
    @NotNull(message = "Cena jest wymagana", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Min(value = 0, message = "Cena nie może być ujemna", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Double price;
    
    @NotBlank(message = "Kategoria jest wymagana", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 2, max = 50, message = "Kategoria musi mieć od 2 do 50 znaków", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String category;
    
    @NotNull(message = "Status dostępności jest wymagany", groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Boolean inStock;

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, String category, Boolean inStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.inStock = inStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }
} 