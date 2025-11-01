package com.medicalstore.repository;

import com.medicalstore.model.OrderItem;
import java.util.List;

public interface OrderItemRepository {
    void saveAll(List<OrderItem> items);
    List<OrderItem> findByOrderId(Integer orderId);
}
