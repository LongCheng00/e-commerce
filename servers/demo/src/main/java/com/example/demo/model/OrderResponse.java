package com.example.demo.model;

public record OrderResponse(
    Long orderId,
    double totalPrice
    // String message
) {}