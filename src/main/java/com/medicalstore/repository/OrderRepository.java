package com.medicalstore.repository;

import com.medicalstore.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Integer save(Order order); // returns generated order id
    void updateStatus(Integer orderId, String status);
    List<Order> findByUserId(Integer userId);
}
