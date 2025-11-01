package com.medicalstore.service.impl;

import com.medicalstore.model.Order;
import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.OrderRepository;
import com.medicalstore.service.InventoryService;
import com.medicalstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional
    public Integer createOrder(Order order) {
        // Save the order
        Integer orderId = orderRepository.save(order);

        // Save order items
        if (orderId != null && order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                orderRepository.saveOrderItem(orderId, item.getMedicineId(), item.getQuantity(), item.getPrice());
            }
        }

        return orderId;
    }

    @Override
    @Transactional
    public boolean checkout(Order order) {
        try {
            // Validate stock availability
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    var inventory = inventoryService.getInventoryByMedicineId(item.getMedicineId());
                    if (inventory == null) {
                        throw new RuntimeException("Medicine not found in inventory: " + item.getMedicineId());
                    }
                    if (inventory.getQuantity() < item.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for medicine ID: " + item.getMedicineId() +
                                ". Available: " + inventory.getQuantity() + ", Requested: " + item.getQuantity());
                    }
                }
            }

            // Create order
            Integer orderId = createOrder(order);
            if (orderId == null) {
                return false;
            }

            // Update inventory
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    var inventory = inventoryService.getInventoryByMedicineId(item.getMedicineId());
                    if (inventory != null) {
                        int newQuantity = inventory.getQuantity() - item.getQuantity();
                        inventoryService.updateInventoryQuantity(item.getMedicineId(), newQuantity);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Checkout failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateOrderStatus(Integer orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}