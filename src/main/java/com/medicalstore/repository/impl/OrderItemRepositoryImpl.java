package com.medicalstore.repository.impl;

import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItem> orderItemRowMapper;

    @Autowired
    public OrderItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemRowMapper = new RowMapper<OrderItem>() {
            @Override
            public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderItem item = new OrderItem();
                item.setItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setMedicineName(rs.getString("medicine_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                return item;
            }
        };
    }

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    @Override
    public void saveAll(List<OrderItem> items) {
        for (OrderItem item : items) {
            save(item);
        }
    }

    @Override
    public void save(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                item.getOrderId(),
                item.getMedicineId(),
                item.getQuantity(),
                item.getPrice());
    }
}