package com.medicalstore.repository;

import com.medicalstore.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

@Repository
public class UserRepositoryJdbcImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setUserId(rs.getLong("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setEmail(rs.getString("email"));
        u.setAddress(rs.getString("address"));
        return u;
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            User u = jdbcTemplate.queryForObject(sql, this::mapRow, id);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer findUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            User u = jdbcTemplate.queryForObject(sql, this::mapRow, username);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY user_id";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public int save(User user) {
        String sql = "INSERT INTO users (username, password, role, email, address) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getEmail(),
                user.getAddress());
    }

    @Override
    public int update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, email = ?, address = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getEmail(),
                user.getAddress(),
                user.getUserId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
