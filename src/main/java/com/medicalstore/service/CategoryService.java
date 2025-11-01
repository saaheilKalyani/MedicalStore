package com.medicalstore.service;

import com.medicalstore.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(int id);
    Optional<Category> getCategoryByName(String name);
    int createCategory(Category category) throws IllegalArgumentException;
    int updateCategory(Category category) throws IllegalArgumentException;
    int deleteCategory(int id);
}
