package com.medicalstore.controller;

import com.medicalstore.model.Order;
import com.medicalstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Integer orderId = orderService.createOrder(order);
            if (orderId != null) {
                return ResponseEntity.ok().body("{\"message\": \"Order created successfully\", \"orderId\": " + orderId + "}");
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Failed to create order\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Order order) {
        try {
            boolean success = orderService.checkout(order);
            if (success) {
                return ResponseEntity.ok().body("{\"message\": \"Checkout successful\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Checkout failed\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Checkout failed: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok().body("{\"message\": \"Order status updated successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to update order status\"}");
        }
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Integer userId) {
        return orderService.getOrdersByUserId(userId);
    }
}