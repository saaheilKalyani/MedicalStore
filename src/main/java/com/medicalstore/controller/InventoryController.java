package com.medicalstore.controller;

import com.medicalstore.model.Inventory;
import com.medicalstore.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Integer inventoryId) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
        return inventory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody Inventory inventory) {
        try {
            Inventory savedInventory = inventoryService.saveInventory(inventory);
            return ResponseEntity.ok(savedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to create inventory: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<?> updateInventory(@PathVariable Integer inventoryId, @RequestBody Inventory inventory) {
        try {
            inventory.setInventoryId(inventoryId);
            Inventory updatedInventory = inventoryService.saveInventory(inventory);
            return ResponseEntity.ok(updatedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to update inventory\"}");
        }
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<?> deleteInventory(@PathVariable Integer inventoryId) {
        try {
            inventoryService.deleteInventory(inventoryId);
            return ResponseEntity.ok().body("{\"message\": \"Inventory deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to delete inventory\"}");
        }
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<Inventory> getInventoryByMedicineId(@PathVariable Integer medicineId) {
        Inventory inventory = inventoryService.getInventoryByMedicineId(medicineId);
        return inventory != null ? ResponseEntity.ok(inventory) : ResponseEntity.notFound().build();
    }

    @PutMapping("/medicine/{medicineId}/quantity")
    public ResponseEntity<?> updateInventoryQuantity(@PathVariable Integer medicineId, @RequestParam Integer quantity) {
        try {
            inventoryService.updateInventoryQuantity(medicineId, quantity);
            return ResponseEntity.ok().body("{\"message\": \"Inventory quantity updated successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to update inventory quantity\"}");
        }
    }

    @GetMapping("/supplier/{supplierId}")
    public List<Inventory> getInventoryBySupplierId(@PathVariable Integer supplierId) {
        return inventoryService.getInventoryBySupplierId(supplierId);
    }
}