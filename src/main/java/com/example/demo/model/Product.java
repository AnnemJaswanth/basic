package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Product description cannot be empty")
    @Size(min = 2, max = 500, message = "Product description must be between 2 and 500 characters")
    private String description;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @PositiveOrZero(message = "Quantity must be greater than or equal to 0")
    private int quantity;

    @NotBlank(message = "Category cannot be empty")
    @Size(min = 2, max = 100, message = "Category must be between 2 and 100 characters")
    private String category;

    @NotBlank(message = "Image URL cannot be empty")
    @Size(max = 500, message = "Image URL must be at most 500 characters")
    private String imageUrl;  

    public Product() {}

    public Product(Long id, String name, String description, double price, int quantity, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description + 
               ", price=" + price + ", quantity=" + quantity + ", category=" + category + 
               ", imageUrl=" + imageUrl + "]";
    }
}
