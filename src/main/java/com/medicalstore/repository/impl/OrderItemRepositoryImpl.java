package com.medicalstore.repository.impl;

import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> {
        OrderItem it = new OrderItem();
        it.setItemId(rs.getInt("item_id"));
        it.setOrderId(rs.getInt("order_id"));
        it.setMedicineId(rs.getInt("medicine_id"));
        it.setQuantity(rs.getInt("quantity"));
        it.setPrice(rs.getBigDecimal("price"));
        return it;
    };

    @Override
    public void saveAll(List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, medicine_id, quantity, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, items, items.size(), (ps, item) -> {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getMedicineId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());
        });
    }

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
