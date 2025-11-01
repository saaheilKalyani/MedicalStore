package com.medicalstore.repository.impl;

import com.medicalstore.model.Order;
import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RowMapper<Order> orderRowMapper;

    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.orderRowMapper = (rs, rowNum) -> {
            Order order = new Order();
            order.setOrderId(rs.getInt("order_id"));
            order.setUserId(rs.getInt("user_id"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));

            Timestamp orderDate = rs.getTimestamp("order_date");
            if (orderDate != null) {
                order.setOrderDate(orderDate.toLocalDateTime());
            }

            order.setStatus(rs.getString("status"));

            // Load order items
            String itemsSql = "SELECT oi.*, m.name as medicine_name FROM order_items oi " +
                    "JOIN medicines m ON oi.medicine_id = m.medicine_id " +
                    "WHERE oi.order_id = ?";
            List<OrderItem> items = jdbcTemplate.query(itemsSql, (itemRs, itemRowNum) -> {
                OrderItem item = new OrderItem();
                item.setItemId(itemRs.getInt("item_id"));
                item.setOrderId(itemRs.getInt("order_id"));
                item.setMedicineId(itemRs.getInt("medicine_id"));
                item.setQuantity(itemRs.getInt("quantity"));
                item.setPrice(itemRs.getBigDecimal("price"));
                item.setMedicineName(itemRs.getString("medicine_name"));
                return item;
            }, order.getOrderId());

            order.setItems(items);
            return order;
        };
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT o.*, u.username as user_name FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "ORDER BY o.order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public Optional<Order> findById(Integer orderId) {
        String sql = "SELECT o.*, u.username as user_name FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.order_id = ?";
        try {
            Order order = jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
            return Optional.of(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, order_date, status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"order_id"}); // FIX: Specify the column name
            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            ps.setString(4, order.getStatus());
            return ps;
        }, keyHolder);

        // Get generated key - FIXED: Use the correct method for PostgreSQL
        Integer orderId = null;
        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("order_id")) {
            orderId = (Integer) keyHolder.getKeys().get("order_id");
        } else if (keyHolder.getKey() != null) {
            orderId = keyHolder.getKey().intValue();
        }

        return orderId;
    }

    @Override
    public void saveOrderItem(Integer orderId, Integer medicineId, Integer quantity, BigDecimal price) {
        String sql = "INSERT INTO order_items (order_id, medicine_id, quantity, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, medicineId, quantity, price);
    }

    @Override
    public void updateStatus(Integer orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, status, orderId);
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        String sql = "SELECT o.*, u.username as user_name FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.user_id = ? ORDER BY o.order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }
}