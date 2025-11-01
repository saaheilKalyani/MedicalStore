package com.medicalstore.service.impl;

import com.medicalstore.model.Order;
import com.medicalstore.repository.OrderRepository;
import com.medicalstore.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Integer placeOrder(Order order) {
        // business validations can be added here (stock checks, price calculations). Keep minimal.
        return orderRepository.save(order);
    }

    @Override
    public void updateStatus(Integer id, String status) {
        orderRepository.updateStatus(id, status);
    }

    @Override
    public List<Order> listByUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}
