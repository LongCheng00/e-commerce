package com.example.demo.model;

import java.util.List;

public record CreateOrderRequest(
    List<OrderItemRequest> items
) {
    public record OrderItemRequest(
        Long productId,
        int quantity
    ) {}
}