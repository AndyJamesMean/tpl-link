package com.example.taobao.controller;

import com.example.taobao.dto.CartItemRequest;
import com.example.taobao.dto.CartResponse;
import com.example.taobao.dto.QuantityRequest;
import com.example.taobao.service.CartService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items")
    public CartResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(request.getProductId(), request.getQuantity());
    }

    @PatchMapping("/items/{productId}")
    public CartResponse updateItem(@PathVariable Long productId, @Valid @RequestBody QuantityRequest request) {
        return cartService.updateItem(productId, request.getQuantity());
    }

    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }

    @DeleteMapping
    public CartResponse clear() {
        return cartService.clear();
    }
}
