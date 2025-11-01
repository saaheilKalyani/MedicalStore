package com.medicalstore.repository.impl;

import com.medicalstore.model.Category;
import com.medicalstore.repository.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Category> rowMapper = (ResultSet rs, int rowNum) -> {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        return c;
    };

    @Override
    public List<Category> findAll() {
        String sql = "SELECT category_id, name, description FROM categories ORDER BY name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT category_id, name, description FROM categories WHERE category_id = ?";
        List<Category> list = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<Category> findByName(String name) {
        String sql = "SELECT category_id, name, description FROM categories WHERE name = ?";
        List<Category> list = jdbcTemplate.query(sql, new Object[]{name}, rowMapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public int save(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public int update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        return jdbcTemplate.update(sql, category.getName(), category.getDescription(), category.getCategoryId());
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
