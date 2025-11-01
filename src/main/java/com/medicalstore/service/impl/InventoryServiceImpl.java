package com.medicalstore.service.impl;

import com.medicalstore.model.Inventory;
import com.medicalstore.repository.InventoryRepository;
import com.medicalstore.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(Integer inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    @Override
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(Integer inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    public Inventory getInventoryByMedicineId(Integer medicineId) {
        return inventoryRepository.findByMedicineId(medicineId);
    }

    @Override
    public void updateInventoryQuantity(Integer medicineId, Integer quantity) {
        inventoryRepository.updateQuantity(medicineId, quantity);
    }

    @Override
    public List<Inventory> getInventoryBySupplierId(Integer supplierId) {
        return inventoryRepository.findBySupplierId(supplierId);
    }
}