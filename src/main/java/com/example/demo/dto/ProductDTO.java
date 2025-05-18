package com.example.demo.dto;

public class ProductDTO {
    private String name;
    private Double price;

    public ProductDTO(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
