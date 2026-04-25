package com.example.taobao.service;

import com.example.taobao.dto.CartItemResponse;
import com.example.taobao.dto.CartResponse;
import com.example.taobao.exception.BusinessException;
import com.example.taobao.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final CatalogService catalogService;
    private final Map<Long, Integer> cartItems = new LinkedHashMap<Long, Integer>();

    public CartService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public synchronized CartResponse getCart() {
        List<CartItemResponse> items = new ArrayList<CartItemResponse>();
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Product product = catalogService.getProduct(entry.getKey());
            int quantity = entry.getValue();
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(quantity));
            items.add(new CartItemResponse(product.getId(), product.getTitle(), product.getShopName(),
                    product.getPrice(), quantity, product.getStock(), subtotal));
            totalQuantity += quantity;
            totalAmount = totalAmount.add(subtotal);
        }

        return new CartResponse(items, totalQuantity, totalAmount);
    }

    public synchronized CartResponse addItem(Long productId, int quantity) {
        Product product = catalogService.getProduct(productId);
        int nextQuantity = cartItems.containsKey(productId) ? cartItems.get(productId) + quantity : quantity;
        ensureStock(product, nextQuantity);
        cartItems.put(productId, nextQuantity);
        return getCart();
    }

    public synchronized CartResponse updateItem(Long productId, int quantity) {
        Product product = catalogService.getProduct(productId);
        if (quantity == 0) {
            cartItems.remove(productId);
            return getCart();
        }
        ensureStock(product, quantity);
        cartItems.put(productId, quantity);
        return getCart();
    }

    public synchronized CartResponse removeItem(Long productId) {
        cartItems.remove(productId);
        return getCart();
    }

    public synchronized CartResponse clear() {
        cartItems.clear();
        return getCart();
    }

    private void ensureStock(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new BusinessException(product.getTitle() + "库存不足，当前仅剩" + product.getStock() + "件");
        }
    }
}
