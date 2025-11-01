package com.medicalstore.repository;

import com.medicalstore.model.OrderItem;
import java.util.List;

public interface OrderItemRepository {
    List<OrderItem> findByOrderId(Integer orderId);
    void saveAll(List<OrderItem> items);
    void save(OrderItem item);
}