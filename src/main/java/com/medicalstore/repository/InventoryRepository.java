package com.medicalstore.repository;

import com.medicalstore.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    List<Inventory> findAll();

    List<Inventory> findBySupplierId(int supplierId);

    Optional<Inventory> findById(int inventoryId);

    Optional<Inventory> findByMedicineAndSupplier(int medicineId, int supplierId);

    int save(Inventory inventory);

    int updateQuantity(int inventoryId, int quantity);

    List<Inventory> findLowStock(int threshold);
}
