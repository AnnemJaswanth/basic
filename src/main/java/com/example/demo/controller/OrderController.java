package com.example.demo.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.OrderDetails;
import com.example.demo.dto.OrderRequest;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            String message = orderService.createOrder(orderRequest , request);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDetails>> getOrdersByUser(HttpServletRequest request) {
        List<OrderDetails> orders = orderService.getOrdersByUser(request);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<OrderDetails>> getAllOrdersForAdmin() {
        List<OrderDetails> orders = orderService.getAllOrdersForAdmin();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetails> getOrderById(@PathVariable Long orderId) {
        OrderDetails order = orderService.getOrderById(orderId,false);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/status-update/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        try {
            String message = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    @GetMapping("/status/{id}")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long id,HttpServletRequest request) {
        try {
            String status = orderService.getOrderStatus(id,request);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
