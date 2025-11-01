package com.medicalstore.repository;

import com.medicalstore.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    Optional<Order> findById(Integer orderId);
    Integer save(Order order);
    void updateStatus(Integer orderId, String status);
    List<Order> findByUserId(Integer userId);
    void saveOrderItem(Integer orderId, Integer medicineId, Integer quantity, BigDecimal price);
}