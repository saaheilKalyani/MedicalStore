package com.medicalstore.model;

import java.math.BigDecimal;

public class OrderItem {
    private Integer itemId;
    private Integer orderId;
    private Integer medicineId;
    private Integer quantity;
    private BigDecimal price;
    private String medicineName; // For display

    // Constructors
    public OrderItem() {}

    public OrderItem(Integer orderId, Integer medicineId, Integer quantity, BigDecimal price) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
}