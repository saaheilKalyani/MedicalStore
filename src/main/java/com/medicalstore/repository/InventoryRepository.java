package com.medicalstore.repository;

import com.medicalstore.model.Inventory;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    List<Inventory> findAll();
    Optional<Inventory> findById(Integer inventoryId);
    Inventory save(Inventory inventory);
    void deleteById(Integer inventoryId);
    Inventory findByMedicineId(Integer medicineId);
    void updateQuantity(Integer medicineId, Integer quantity);
    List<Inventory> findBySupplierId(Integer supplierId);
}