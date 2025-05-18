package com.example.demo.dto;

import java.util.List;

public class OrderDetails {
    private Long orderId;
    private List<ProductDTO> products;
    private Double totalPrice;
    private String status;
    private Long userId; 

    public OrderDetails(Long orderId, List<ProductDTO> products, String status, Double totalPrice) {
        this.orderId = orderId;
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice;
        this.userId = null;
    }

    public OrderDetails(Long orderId, Long userId, List<ProductDTO> products, String status, Double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.products = products;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }
}
