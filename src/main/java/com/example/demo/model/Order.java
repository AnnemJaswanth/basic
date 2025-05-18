package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    private List<Long> productIds;
    private Double totalPrice;
    private String status;

    private LocalDateTime createdAt;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING"; 
    }

    public Order(User user, List<Long> productIds, Double totalPrice) {
        this.user = user;
        this.productIds = productIds;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<Long> getProductIds() { return productIds; }
    public Double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
    public void setUser(User user) { this.user = user; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                ", productIds=" + productIds +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return Objects.equals(id, order.id) &&
               Objects.equals(user, order.user) &&
               Objects.equals(productIds, order.productIds) &&
               Objects.equals(totalPrice, order.totalPrice) &&
               Objects.equals(status, order.status) &&
               Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, productIds, totalPrice, status, createdAt);
    }
}
