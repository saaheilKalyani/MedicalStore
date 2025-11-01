package com.medicalstore.repository.impl;

import com.medicalstore.model.Order;
import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.OrderRepository;
import com.medicalstore.repository.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final OrderItemRepository orderItemRepository;
    private final RowMapper<Order> orderRowMapper;  // ✅ declare field properly here

    // ✅ Constructor — initializes all fields
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate, OrderItemRepository orderItemRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemRepository = orderItemRepository;

        // initialize RowMapper after dependencies are injected
        this.orderRowMapper = (rs, rowNum) -> {
            Order o = new Order();
            o.setOrderId(rs.getInt("order_id"));
            o.setUserId(rs.getInt("user_id"));
            o.setTotalAmount(rs.getBigDecimal("total_amount"));
            Timestamp ts = rs.getTimestamp("order_date");
            if (ts != null) {
                o.setOrderDate(ts.toLocalDateTime());
            }
            o.setStatus(rs.getString("status"));

            // lazy load items for this order
            o.setItems(orderItemRepository.findByOrderId(o.getOrderId()));
            return o;
        };
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public Optional<Order> findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        List<Order> list = jdbcTemplate.query(sql, orderRowMapper, id);
        // ✅ use get(0) safely
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Integer save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, order_date, status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO);
            ps.setObject(3, order.getOrderDate() != null ?
                    Timestamp.valueOf(order.getOrderDate()) :
                    new Timestamp(System.currentTimeMillis()));
            ps.setString(4, order.getStatus() != null ? order.getStatus() : "PENDING");
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        Integer orderId = (key != null) ? key.intValue() : null;

        // save order items if available
        if (orderId != null && order.getItems() != null && !order.getItems().isEmpty()) {
            for (OrderItem item : order.getItems()) {
                item.setOrderId(orderId);
            }
            orderItemRepository.saveAll(order.getItems());
        }

        return orderId;
    }

    @Override
    public void updateStatus(Integer orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, status, orderId);
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }
}
