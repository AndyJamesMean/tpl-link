package com.example.taobao.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartResponse {

    private List<CartItemResponse> items = new ArrayList<CartItemResponse>();
    private int totalQuantity;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public CartResponse() {
    }

    public CartResponse(List<CartItemResponse> items, int totalQuantity, BigDecimal totalAmount) {
        this.items = items;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
