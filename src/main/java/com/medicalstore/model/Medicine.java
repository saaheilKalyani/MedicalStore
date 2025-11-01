package com.medicalstore.model;

import java.sql.Date;

public class Medicine {
    private int medicineId;
    private String name;
    private int categoryId;
    private double price;
    private int quantity;
    private int supplierId;
    private Date expiryDate;

    // Getters and Setters
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}
