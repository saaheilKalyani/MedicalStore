package com.medicalstore.service;

import com.medicalstore.model.Inventory;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventory();
    Optional<Inventory> getInventoryById(Integer inventoryId);
    Inventory saveInventory(Inventory inventory);
    void deleteInventory(Integer inventoryId);
    Inventory getInventoryByMedicineId(Integer medicineId);
    void updateInventoryQuantity(Integer medicineId, Integer quantity);
    List<Inventory> getInventoryBySupplierId(Integer supplierId);
}