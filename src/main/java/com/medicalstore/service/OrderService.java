package com.medicalstore.service;

import com.medicalstore.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> listAll();
    Optional<Order> getById(Integer id);
    Integer placeOrder(Order order);
    void updateStatus(Integer id, String status);
    List<Order> listByUser(Integer userId);
}
