package com.medicalstore.service;

import com.medicalstore.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Integer orderId);
    Integer createOrder(Order order);
    void updateOrderStatus(Integer orderId, String status);
    List<Order> getOrdersByUserId(Integer userId);
    boolean checkout(Order order);
}