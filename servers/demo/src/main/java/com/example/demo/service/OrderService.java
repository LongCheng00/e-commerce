package com.example.demo.service;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    Optional<Order> getOrderById(Long orderId);
    List<Order> getAllOrders();
}