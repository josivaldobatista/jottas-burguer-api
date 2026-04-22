package com.jfb.jottasburger.web.dto;

import java.math.BigDecimal;

public class CartItem {

    private final Long productId;
    private final String name;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(Long productId, String name, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}