package com.medicalstore.repository.impl;

import com.medicalstore.model.Inventory;
import com.medicalstore.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Inventory> inventoryRowMapper;

    @Autowired
    public InventoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.inventoryRowMapper = (rs, rowNum) -> {
            Inventory inventory = new Inventory();
            inventory.setInventoryId(rs.getInt("inventory_id"));
            inventory.setSupplierId(rs.getInt("supplier_id"));
            inventory.setMedicineId(rs.getInt("medicine_id"));
            inventory.setQuantity(rs.getInt("quantity"));

            Timestamp lastUpdated = rs.getTimestamp("last_updated");
            if (lastUpdated != null) {
                inventory.setLastUpdated(lastUpdated.toLocalDateTime());
            }

            try {
                inventory.setMedicineName(rs.getString("medicine_name"));
            } catch (Exception e) {
                // Ignore if column doesn't exist
            }

            try {
                inventory.setSupplierName(rs.getString("supplier_name"));
            } catch (Exception e) {
                // Ignore if column doesn't exist
            }

            return inventory;
        };
    }

    @Override
    public List<Inventory> findAll() {
        String sql = "SELECT i.*, m.name as medicine_name, u.username as supplier_name " +
                "FROM inventory i " +
                "JOIN medicines m ON i.medicine_id = m.medicine_id " +
                "JOIN users u ON i.supplier_id = u.user_id " +
                "ORDER BY i.last_updated DESC";
        return jdbcTemplate.query(sql, inventoryRowMapper);
    }

    @Override
    public Optional<Inventory> findById(Integer inventoryId) {
        String sql = "SELECT i.*, m.name as medicine_name, u.username as supplier_name " +
                "FROM inventory i " +
                "JOIN medicines m ON i.medicine_id = m.medicine_id " +
                "JOIN users u ON i.supplier_id = u.user_id " +
                "WHERE i.inventory_id = ?";
        try {
            Inventory inventory = jdbcTemplate.queryForObject(sql, inventoryRowMapper, inventoryId);
            return Optional.of(inventory);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Inventory save(Inventory inventory) {
        if (inventory.getInventoryId() == null) {
            // Insert new inventory
            String sql = "INSERT INTO inventory (supplier_id, medicine_id, quantity, last_updated) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"inventory_id"}); // FIX: Specify the column name
                ps.setInt(1, inventory.getSupplierId());
                ps.setInt(2, inventory.getMedicineId());
                ps.setInt(3, inventory.getQuantity());
                ps.setTimestamp(4, Timestamp.valueOf(inventory.getLastUpdated()));
                return ps;
            }, keyHolder);

            // Get generated key - FIXED: Use the correct method for PostgreSQL
            if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("inventory_id")) {
                inventory.setInventoryId((Integer) keyHolder.getKeys().get("inventory_id"));
            } else if (keyHolder.getKey() != null) {
                inventory.setInventoryId(keyHolder.getKey().intValue());
            }
        } else {
            // Update existing inventory
            String sql = "UPDATE inventory SET supplier_id = ?, medicine_id = ?, quantity = ?, last_updated = ? WHERE inventory_id = ?";
            jdbcTemplate.update(sql,
                    inventory.getSupplierId(),
                    inventory.getMedicineId(),
                    inventory.getQuantity(),
                    Timestamp.valueOf(inventory.getLastUpdated()),
                    inventory.getInventoryId());
        }

        // Update medicine quantity
        updateMedicineQuantity(inventory.getMedicineId(), inventory.getQuantity());
        return inventory;
    }

    @Override
    public void deleteById(Integer inventoryId) {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";
        jdbcTemplate.update(sql, inventoryId);
    }

    @Override
    public Inventory findByMedicineId(Integer medicineId) {
        String sql = "SELECT i.*, m.name as medicine_name, u.username as supplier_name " +
                "FROM inventory i " +
                "JOIN medicines m ON i.medicine_id = m.medicine_id " +
                "JOIN users u ON i.supplier_id = u.user_id " +
                "WHERE i.medicine_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, inventoryRowMapper, medicineId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateQuantity(Integer medicineId, Integer quantity) {
        String sql = "UPDATE inventory SET quantity = ?, last_updated = CURRENT_TIMESTAMP WHERE medicine_id = ?";
        jdbcTemplate.update(sql, quantity, medicineId);

        // Also update medicine quantity
        updateMedicineQuantity(medicineId, quantity);
    }

    @Override
    public List<Inventory> findBySupplierId(Integer supplierId) {
        String sql = "SELECT i.*, m.name as medicine_name, u.username as supplier_name " +
                "FROM inventory i " +
                "JOIN medicines m ON i.medicine_id = m.medicine_id " +
                "JOIN users u ON i.supplier_id = u.user_id " +
                "WHERE i.supplier_id = ? ORDER BY i.last_updated DESC";
        return jdbcTemplate.query(sql, inventoryRowMapper, supplierId);
    }

    private void updateMedicineQuantity(Integer medicineId, Integer quantity) {
        String sql = "UPDATE medicines SET quantity = ? WHERE medicine_id = ?";
        jdbcTemplate.update(sql, quantity, medicineId);
    }
}