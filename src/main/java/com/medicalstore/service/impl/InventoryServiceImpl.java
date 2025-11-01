package com.medicalstore.service.impl;

import com.medicalstore.model.Inventory;
import com.medicalstore.repository.InventoryRepository;
import com.medicalstore.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repo;

    public InventoryServiceImpl(InventoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Inventory> listAll() {
        return repo.findAll();
    }

    @Override
    public List<Inventory> listBySupplier(int supplierId) {
        return repo.findBySupplierId(supplierId);
    }

    @Override
    public Optional<Inventory> getById(int inventoryId) {
        return repo.findById(inventoryId);
    }

    @Override
    public Optional<Inventory> getByMedicineAndSupplier(int medicineId, int supplierId) {
        return repo.findByMedicineAndSupplier(medicineId, supplierId);
    }

    @Override
    public Integer addInventory(Inventory inventory) {
        return repo.save(inventory);
    }

    @Override
    public void updateStock(int inventoryId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        repo.updateQuantity(inventoryId, newQuantity);
    }

    @Override
    public List<Inventory> listLowStock(int threshold) {
        return repo.findLowStock(threshold);
    }

    @Override
    public boolean existsByMedicineAndSupplier(int medicineId, int supplierId) {
        return repo.existsByMedicineAndSupplier(medicineId, supplierId);
    }
}