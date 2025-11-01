package com.medicalstore.repository;

import com.medicalstore.model.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicineRepositoryImpl implements MedicineRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Medicine> findAll() {
        String sql = "SELECT * FROM medicines ORDER BY medicine_id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class));
    }

    @Override
    public Medicine findById(int id) {
        String sql = "SELECT * FROM medicines WHERE medicine_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Medicine.class), id);
    }

    @Override
    public int save(Medicine medicine) {
        String sql = "INSERT INTO medicines (name, category_id, price, quantity, supplier_id, expiry_date) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                medicine.getName(),
                medicine.getCategoryId(),
                medicine.getPrice(),
                medicine.getQuantity(),
                medicine.getSupplierId(),
                medicine.getExpiryDate());
    }

    @Override
    public int update(Medicine medicine) {
        String sql = "UPDATE medicines SET name=?, category_id=?, price=?, quantity=?, supplier_id=?, expiry_date=? WHERE medicine_id=?";
        return jdbcTemplate.update(sql,
                medicine.getName(),
                medicine.getCategoryId(),
                medicine.getPrice(),
                medicine.getQuantity(),
                medicine.getSupplierId(),
                medicine.getExpiryDate(),
                medicine.getMedicineId());
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM medicines WHERE medicine_id=?";
        return jdbcTemplate.update(sql, id);
    }
}
