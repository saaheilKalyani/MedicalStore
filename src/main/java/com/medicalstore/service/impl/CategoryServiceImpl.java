package com.medicalstore.service.impl;

import com.medicalstore.model.Category;
import com.medicalstore.repository.CategoryRepository;
import com.medicalstore.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional
    public int createCategory(Category category) throws IllegalArgumentException {
        // basic validation
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty.");
        }
        // uniqueness check
        Optional<Category> existing = categoryRepository.findByName(category.getName().trim());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Category with this name already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public int updateCategory(Category category) throws IllegalArgumentException {
        if (category.getCategoryId() == null) {
            throw new IllegalArgumentException("Category id is required for update.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty.");
        }
        // Prevent duplicate names with different id
        Optional<Category> byName = categoryRepository.findByName(category.getName().trim());
        if (byName.isPresent() && !byName.get().getCategoryId().equals(category.getCategoryId())) {
            throw new IllegalArgumentException("Another category with this name already exists.");
        }
        return categoryRepository.update(category);
    }

    @Override
    @Transactional
    public int deleteCategory(int id) {
        return categoryRepository.deleteById(id);
    }
}
