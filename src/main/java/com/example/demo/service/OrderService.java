package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDetails;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.ProductDTO;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final JwtService jwtService;

    public OrderService(OrderRepo orderRepo, UserRepo userRepo, ProductRepo productRepo,JwtService jwtService) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.jwtService = jwtService;
    }

    public String createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        List<Long> productIds = orderRequest.getProductIds();
    
        Map<Long, Integer> productQuantities = new HashMap<>();
        
        List<Product> products = productRepo.findAllById(productIds);
        if (products.isEmpty() || products.size() != productIds.size()) {
            throw new ProductNotFoundException("Some products not found with given IDs.");
        }
        for (Product product : products) {
            productQuantities.put(product.getId(), productQuantities.getOrDefault(product.getId(), 0) + 1);
        }
    
        double totalPrice = 0.0;
        for (Product product : products) {
            int requestedQuantity = productQuantities.get(product.getId());
            if (product.getQuantity() < requestedQuantity) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - requestedQuantity);
            totalPrice += product.getPrice() * requestedQuantity;
        }
    
        productRepo.saveAll(products);
    
        Order order = new Order(user, productIds, totalPrice);
        orderRepo.save(order);
    
        return String.format("Order created successfully. Total price: %.2f", totalPrice);
    }
    

    public List<OrderDetails> getOrdersByUser(HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        List<Order> orders = orderRepo.findByUserId(user.getId());
        return orders.stream().map(order -> convertToOrderDetails(order, false)).collect(Collectors.toList());
    }

    public List<OrderDetails> getAllOrdersForAdmin() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(order -> convertToOrderDetails(order, true)).collect(Collectors.toList());
    }

    public OrderDetails getOrderById(Long orderId, boolean isAdmin) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return convertToOrderDetails(order, isAdmin);
    }

    public String updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status);
        orderRepo.save(order);
        convertToOrderDetails(order, true);
        return "Status updated successfully.";
    }

    public String getOrderStatus(Long orderId, HttpServletRequest request) {
        User currentUser = getAuthenticatedUser(request);
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (currentUser.getRole().equals("ADMIN") || order.getUser().getId().equals(currentUser.getId())) {
            return order.getStatus();
        } else {
            throw new RuntimeException("Access denied: You are not authorized to view this order status.");
        }
    }

    private OrderDetails convertToOrderDetails(Order order, boolean isAdmin) {
        List<ProductDTO> productDTOs = productRepo.findAllById(order.getProductIds()).stream()
                .map(product -> new ProductDTO(product.getName(), product.getPrice()))
                .collect(Collectors.toList());

        if (isAdmin) {
            return new OrderDetails(order.getId(), order.getUser().getId(), productDTOs, order.getStatus(),
                    order.getTotalPrice());
        } else {
            return new OrderDetails(order.getId(), productDTOs, order.getStatus(), order.getTotalPrice());
        }
    }

    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = jwtService.extractToken(request); 
        if (token == null) {
            throw new RuntimeException("Invalid or missing token.");
        }

        String email = jwtService.extractEmail(token); 
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}