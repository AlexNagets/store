package com.store.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

import static java.math.RoundingMode.UNNECESSARY;

@AllArgsConstructor
public class DisplayProductDto {
    private final Long entityId;
    private int productInCartId;
    private final String title;
    private int quantity;
    private BigDecimal finalProductsPrice;

    public void increaseQuantity(int quantity, BigDecimal productPrice) {
        this.quantity += quantity;
        this.finalProductsPrice = productPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    @JsonIgnore
    @JsonProperty(value = "entityId")
    public Long getEntityId() {
        return entityId;
    }

    public int getProductInCartId() {
        return productInCartId;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getFinalProductsPrice() {
        return finalProductsPrice;
    }

    public void setProductIdInCartAfterRemoving(int newIdInCart) {
        this.productInCartId = newIdInCart;
    }

    public void setNewQuantity(int newQuantity) {
        this.finalProductsPrice =
                this.finalProductsPrice.multiply(BigDecimal.valueOf(newQuantity))
                                       .divide(BigDecimal.valueOf(this.quantity), UNNECESSARY);
        this.quantity = newQuantity;
    }
}
