
package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
    Long id,
    List<OrderItem> items,
    double totalPrice,
    LocalDateTime createdAt
) {}