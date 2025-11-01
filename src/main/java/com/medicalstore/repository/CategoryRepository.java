package com.medicalstore.repository;

import com.medicalstore.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(int id);
    Optional<Category> findByName(String name);
    int save(Category category);        // returns generated id or number of rows affected
    int update(Category category);      // returns rows affected
    int deleteById(int id);             // returns rows affected
}
