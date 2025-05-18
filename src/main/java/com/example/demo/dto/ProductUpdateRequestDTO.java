package com.example.demo.dto;

import java.util.Optional;

public class ProductUpdateRequestDTO {
    private Optional<String> name = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Optional<Double> price = Optional.empty();
    private Optional<Integer> quantity = Optional.empty();
    private Optional<String> category = Optional.empty();
    private Optional<String> imageUrl = Optional.empty();

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<Double> getPrice() {
        return price;
    }

    public Optional<Integer> getQuantity() {
        return quantity;
    }

    public Optional<String> getCategory() {
        return category;
    }

    public Optional<String> getImageUrl() {
        return imageUrl;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public void setPrice(Optional<Double> price) {
        this.price = price;
    }

    public void setQuantity(Optional<Integer> quantity) {
        this.quantity = quantity;
    }

    public void setCategory(Optional<String> category) {
        this.category = category;
    }

    public void setImageUrl(Optional<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
