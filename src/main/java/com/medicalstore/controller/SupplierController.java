package com.medicalstore.controller;

import com.medicalstore.model.SupplierDto;
import com.medicalstore.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // List suppliers - Admin only
    @GetMapping("/suppliers")
    public String viewSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.listAllSuppliers());
        return "suppliers/list";
    }

    // Add supplier form - Admin
    @GetMapping("/supplier/add")
    public String addSupplierForm(Model model) {
        model.addAttribute("supplierDto", new SupplierDto());
        return "suppliers/add";
    }

    // Save supplier - Admin
    @PostMapping("/supplier/add")
    public String saveSupplier(@Valid @ModelAttribute("supplierDto") SupplierDto supplierDto,
                               BindingResult br,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            return "suppliers/add";
        }

        // Check for duplicate username
        if (supplierService.usernameExists(supplierDto.getUsername())) {
            br.rejectValue("username", "duplicate", "Username already exists");
            return "suppliers/add";
        }

        // Check for duplicate email
        if (supplierService.emailExists(supplierDto.getEmail())) {
            br.rejectValue("email", "duplicate", "Email already exists");
            return "suppliers/add";
        }

        try {
            supplierService.addSupplier(supplierDto);
            redirectAttributes.addFlashAttribute("success", "Supplier added successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "suppliers/add";
        }

        return "redirect:/suppliers";
    }
}