package com.medicalstore.repository.impl;

import com.medicalstore.model.SupplierDto;
import com.medicalstore.repository.SupplierRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public SupplierRepositoryImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    private final RowMapper<SupplierDto> mapper = (rs, rowNum) -> {
        SupplierDto s = new SupplierDto();
        s.setUserId(rs.getInt("user_id"));
        s.setUsername(rs.getString("username"));
        s.setEmail(rs.getString("email"));
        s.setAddress(rs.getString("address"));
        return s;
    };

    @Override
    public List<SupplierDto> findAllSuppliers() {
        String sql = "SELECT user_id, username, email, address FROM users WHERE role = 'SUPPLIER' ORDER BY username";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Optional<SupplierDto> findById(int userId) {
        String sql = "SELECT user_id, username, email, address FROM users WHERE user_id = ? AND role = 'SUPPLIER'";
        List<SupplierDto> list = jdbcTemplate.query(sql, mapper, userId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<SupplierDto> findByUsername(String username) {
        String sql = "SELECT user_id, username, email, address FROM users WHERE username = ? AND role = 'SUPPLIER'";
        List<SupplierDto> list = jdbcTemplate.query(sql, mapper, username);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<SupplierDto> findByEmail(String email) {
        String sql = "SELECT user_id, username, email, address FROM users WHERE email = ? AND role = 'SUPPLIER'";
        List<SupplierDto> list = jdbcTemplate.query(sql, mapper, email);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Integer saveSupplier(SupplierDto supplier) {
        String encodedPassword = passwordEncoder.encode(supplier.getPassword());
        String sql = "INSERT INTO users (username, password, role, email, address) VALUES (?, ?, 'SUPPLIER', ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplier.getUsername());
            ps.setString(2, encodedPassword);
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getAddress());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND role = 'SUPPLIER'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND role = 'SUPPLIER'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}