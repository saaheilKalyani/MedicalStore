package com.medicalstore.model;

import java.time.LocalDateTime;

public class Inventory {
    private Integer inventoryId;
    private Integer supplierId;
    private Integer medicineId;
    private Integer quantity;
    private LocalDateTime lastUpdated;
    private String medicineName;
    private String supplierName;

    // Constructors
    public Inventory() {
        this.lastUpdated = LocalDateTime.now(); // Initialize with current time
    }

    public Inventory(Integer supplierId, Integer medicineId, Integer quantity) {
        this.supplierId = supplierId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getLastUpdated() {
        if (lastUpdated == null) {
            lastUpdated = LocalDateTime.now();
        }
        return lastUpdated;
    }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}