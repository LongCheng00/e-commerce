package com.example.demo.service;

import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}