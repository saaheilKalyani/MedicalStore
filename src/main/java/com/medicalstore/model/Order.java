package com.medicalstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Integer orderId;
    private Integer userId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItem> items;
    private String userName;

    // Constructors
    public Order() {
        this.orderDate = LocalDateTime.now(); // Initialize with current time
        this.status = "PENDING"; // Default status
    }

    public Order(Integer userId, BigDecimal totalAmount, String status) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            return BigDecimal.ZERO;
        }
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getOrderDate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() {
        if (status == null) {
            status = "PENDING";
        }
        return status;
    }
    public void setStatus(String status) { this.status = status; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}