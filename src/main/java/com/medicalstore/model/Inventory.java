package com.medicalstore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Inventory {
    private Integer inventoryId;

    @NotNull(message = "Supplier ID required")
    private Integer supplierId;

    @NotNull(message = "Medicine ID required")
    private Integer medicineId;

    @NotNull(message = "Quantity required")
    @Min(value = 0, message = "Quantity must be >= 0")
    private Integer quantity;

    private LocalDateTime lastUpdated;

    // Constructors
    public Inventory() {}

    public Inventory(Integer inventoryId, Integer supplierId, Integer medicineId, Integer quantity, LocalDateTime lastUpdated) {
        this.inventoryId = inventoryId;
        this.supplierId = supplierId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    // getters & setters
    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}