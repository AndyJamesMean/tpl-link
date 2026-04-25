package com.example.taobao.model;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String title;
    private String description;
    private String shopName;
    private BigDecimal price;
    private int stock;
    private int soldCount;
    private String tag;
    private String imageTheme;

    public Product() {
    }

    public Product(Long id, String title, String description, String shopName, BigDecimal price,
                   int stock, int soldCount, String tag, String imageTheme) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.shopName = shopName;
        this.price = price;
        this.stock = stock;
        this.soldCount = soldCount;
        this.tag = tag;
        this.imageTheme = imageTheme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImageTheme() {
        return imageTheme;
    }

    public void setImageTheme(String imageTheme) {
        this.imageTheme = imageTheme;
    }
}
