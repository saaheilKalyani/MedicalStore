package com.medicalstore.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicine {
    private Integer medicineId;
    private String name;
    private Integer categoryId;
    private BigDecimal price;
    private Integer quantity;
    private Integer supplierId;
    private LocalDate expiryDate;
    private String categoryName; // For display

    // Constructors
    public Medicine() {}

    public Medicine(String name, Integer categoryId, BigDecimal price, Integer quantity, Integer supplierId, LocalDate expiryDate) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}