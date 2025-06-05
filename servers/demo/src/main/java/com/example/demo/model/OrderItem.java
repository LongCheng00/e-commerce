
package com.example.demo.model;

public record OrderItem(
    Long productId,
    int quantity,
    double unitPrice
) {}
