package com.medicalstore.service;

import com.medicalstore.model.Inventory;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> listAll();
    List<Inventory> listBySupplier(int supplierId);
    Optional<Inventory> getById(int inventoryId);
    Optional<Inventory> getByMedicineAndSupplier(int medicineId, int supplierId);
    Integer addInventory(Inventory inventory);
    void updateStock(int inventoryId, int newQuantity);
    List<Inventory> listLowStock(int threshold);
    boolean existsByMedicineAndSupplier(int medicineId, int supplierId);
}