package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductUpdateRequestDTO;
import com.example.demo.model.Product;
import com.example.demo.repo.ProductRepo;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepo.findByCategory(category);
    }

    public String updateProduct(Long id, ProductUpdateRequestDTO updateRequest) {
        return productRepo.findById(id).map(existingProduct -> {
            updateRequest.getName().ifPresent(existingProduct::setName);
            updateRequest.getDescription().ifPresent(existingProduct::setDescription);
            updateRequest.getPrice().ifPresent(existingProduct::setPrice);
            updateRequest.getQuantity().ifPresent(existingProduct::setQuantity);
            updateRequest.getCategory().ifPresent(existingProduct::setCategory);
            updateRequest.getImageUrl().ifPresent(existingProduct::setImageUrl);

            productRepo.save(existingProduct);
            return "Product updated successfully!";
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }
}
