package com.medicalstore.controller;

import com.medicalstore.model.Inventory;
import com.medicalstore.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // View all stock - Admin and Supplier
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
    public String viewInventory(Model model) {
        model.addAttribute("inventories", inventoryService.listAll());
        return "inventory/list";
    }

    // Edit quantity form - Supplier only
    @GetMapping("/update/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String updateStockForm(@PathVariable("id") int id, Model model) {
        var invOpt = inventoryService.getById(id);
        if (invOpt.isEmpty()) {
            model.addAttribute("error", "Inventory entry not found");
            return "redirect:/inventory";
        }
        model.addAttribute("inventory", invOpt.get());
        return "inventory/update";
    }

    // Save updated stock - Supplier only
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String updateStock(@PathVariable("id") int id,
                              @Valid @ModelAttribute("inventory") Inventory inventory,
                              BindingResult br,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            return "inventory/update";
        }

        // Verify the inventory exists
        var existingOpt = inventoryService.getById(id);
        if (existingOpt.isEmpty()) {
            model.addAttribute("error", "Inventory entry not found");
            return "inventory/update";
        }

        try {
            inventoryService.updateStock(id, inventory.getQuantity());
            redirectAttributes.addFlashAttribute("success", "Stock updated successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "inventory/update";
        }

        return "redirect:/inventory";
    }

    // Admin: show low stock
    @GetMapping("/low")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewLowStock(@RequestParam(value = "threshold", required = false, defaultValue = "10") int threshold,
                               Model model) {
        if (threshold < 0) {
            threshold = 10;
        }
        model.addAttribute("threshold", threshold);
        model.addAttribute("inventories", inventoryService.listLowStock(threshold));
        return "inventory/low";
    }
}