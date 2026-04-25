package com.example.taobao.service;

import com.example.taobao.dto.CartItemResponse;
import com.example.taobao.dto.CartResponse;
import com.example.taobao.dto.CheckoutRequest;
import com.example.taobao.exception.BusinessException;
import com.example.taobao.model.Order;
import com.example.taobao.model.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private static final DateTimeFormatter ORDER_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final CatalogService catalogService;
    private final CartService cartService;
    private final Map<String, Order> orders = new LinkedHashMap<String, Order>();
    private final AtomicInteger sequence = new AtomicInteger(1000);

    public OrderService(CatalogService catalogService, CartService cartService) {
        this.catalogService = catalogService;
        this.cartService = cartService;
    }

    public synchronized Order createOrder(CheckoutRequest request) {
        CartResponse cart = cartService.getCart();
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("购物车为空，请先选择商品");
        }

        Map<Long, Integer> quantities = toQuantities(cart);
        catalogService.decreaseStocks(quantities);

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (CartItemResponse item : cart.getItems()) {
            orderItems.add(new OrderItem(item.getProductId(), item.getTitle(), item.getShopName(),
                    item.getQuantity(), item.getUnitPrice(), item.getSubtotal()));
        }

        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(nextOrderNo(now), request.getBuyerName(), request.getPhone(),
                request.getAddress(), request.getRemark(), "待支付", cart.getTotalAmount(), now, orderItems);
        orders.put(order.getOrderNo(), order);
        cartService.clear();
        return order;
    }

    public synchronized List<Order> listOrders() {
        List<Order> result = new ArrayList<Order>(orders.values());
        Collections.reverse(result);
        return result;
    }

    public synchronized Order getOrder(String orderNo) {
        Order order = orders.get(orderNo);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    private Map<Long, Integer> toQuantities(CartResponse cart) {
        Map<Long, Integer> quantities = new LinkedHashMap<Long, Integer>();
        for (CartItemResponse item : cart.getItems()) {
            quantities.put(item.getProductId(), item.getQuantity());
        }
        return quantities;
    }

    private String nextOrderNo(LocalDateTime time) {
        int number = sequence.incrementAndGet();
        if (number > 9999) {
            sequence.set(1000);
            number = sequence.incrementAndGet();
        }
        return "TB" + ORDER_TIME_FORMAT.format(time) + number;
    }
}
