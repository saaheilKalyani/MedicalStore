package com.medicalstore.model;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItem {
    private Integer itemId;

    @NotNull(message = "orderId required")
    private Integer orderId;

    @NotNull(message = "medicineId required")
    private Integer medicineId;

    @Min(value = 1, message = "quantity must be >= 1")
    private Integer quantity;

    @NotNull(message = "price required")
    private BigDecimal price;

    // getters / setters
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
}
