package com.example.taobao.dto;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long productId;
    private String title;
    private String shopName;
    private BigDecimal unitPrice;
    private int quantity;
    private int stock;
    private BigDecimal subtotal;

    public CartItemResponse() {
    }

    public CartItemResponse(Long productId, String title, String shopName, BigDecimal unitPrice,
                            int quantity, int stock, BigDecimal subtotal) {
        this.productId = productId;
        this.title = title;
        this.shopName = shopName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.stock = stock;
        this.subtotal = subtotal;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
