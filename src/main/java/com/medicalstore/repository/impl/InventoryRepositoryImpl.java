package com.medicalstore.repository.impl;

import com.medicalstore.model.Inventory;
import com.medicalstore.repository.InventoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public InventoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Inventory> mapper = (rs, rowNum) -> {
        Inventory i = new Inventory();
        i.setInventoryId(rs.getInt("inventory_id"));
        i.setSupplierId(rs.getInt("supplier_id"));
        i.setMedicineId(rs.getInt("medicine_id"));
        i.setQuantity(rs.getInt("quantity"));
        var ts = rs.getTimestamp("last_updated");
        if (ts != null) i.setLastUpdated(ts.toLocalDateTime());
        return i;
    };

    @Override
    public List<Inventory> findAll() {
        String sql = "SELECT * FROM inventory ORDER BY last_updated DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Inventory> findBySupplierId(int supplierId) {
        String sql = "SELECT * FROM inventory WHERE supplier_id = ? ORDER BY last_updated DESC";
        return jdbcTemplate.query(sql, mapper, supplierId);
    }

    @Override
    public Optional<Inventory> findById(int inventoryId) {
        String sql = "SELECT * FROM inventory WHERE inventory_id = ?";
        List<Inventory> list = jdbcTemplate.query(sql, mapper, inventoryId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Inventory> findByMedicineAndSupplier(int medicineId, int supplierId) {
        String sql = "SELECT * FROM inventory WHERE medicine_id = ? AND supplier_id = ?";
        List<Inventory> list = jdbcTemplate.query(sql, mapper, medicineId, supplierId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Integer save(Inventory inventory) {
        String sql = "INSERT INTO inventory (supplier_id, medicine_id, quantity, last_updated) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, inventory.getSupplierId());
            ps.setInt(2, inventory.getMedicineId());
            ps.setInt(3, inventory.getQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public int updateQuantity(int inventoryId, int quantity) {
        String sql = "UPDATE inventory SET quantity = ?, last_updated = ? WHERE inventory_id = ?";
        return jdbcTemplate.update(sql, quantity, Timestamp.valueOf(LocalDateTime.now()), inventoryId);
    }

    @Override
    public List<Inventory> findLowStock(int threshold) {
        String sql = "SELECT * FROM inventory WHERE quantity <= ? ORDER BY quantity ASC";
        return jdbcTemplate.query(sql, mapper, threshold);
    }

    @Override
    public boolean existsByMedicineAndSupplier(int medicineId, int supplierId) {
        String sql = "SELECT COUNT(*) FROM inventory WHERE medicine_id = ? AND supplier_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, medicineId, supplierId);
        return count != null && count > 0;
    }
}