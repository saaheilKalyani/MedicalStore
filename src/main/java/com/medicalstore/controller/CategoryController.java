package com.medicalstore.controller;

import com.medicalstore.model.Category;
import com.medicalstore.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. List categories - publicly viewable by all roles
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories/list"; // templates/categories/list.html
    }

    // 2. Show add form - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/add"; // templates/categories/add.html
    }

    // 3. Save new category - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String saveCategory(@ModelAttribute("category") @Valid Category category,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/add";
        }
        try {
            categoryService.createCategory(category);
            return "redirect:/categories?success";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "categories/add";
        }
    }

    // 4. Show edit form - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable("id") int id, Model model) {
        Optional<Category> opt = categoryService.getCategoryById(id);
        if (opt.isEmpty()) {
            return "redirect:/categories?notfound";
        }
        model.addAttribute("category", opt.get());
        return "categories/edit"; // templates/categories/edit.html
    }

    // 5. Update category - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") int id,
                                 @ModelAttribute("category") @Valid Category category,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/edit";
        }
        category.setCategoryId(id);
        try {
            categoryService.updateCategory(category);
            return "redirect:/categories?updated";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "categories/edit";
        }
    }

    // 6. Delete category - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories?deleted";
    }
}
