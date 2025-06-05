package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Product;
import com.example.demo.service.OrderService;
import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderResponse;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest req) {
    // Logic to create an order
    var orderResponse = orderService.createOrder(req);
    return ResponseEntity.ok(orderResponse);
  } 

  // @GetMapping("/{id}")
  // public ResponseEntity<Product> getProductById(@PathVariable Long id) {
  //   return shopService.getProductById(id)
  //       .map(ResponseEntity::ok)
  //       .orElseGet(() -> ResponseEntity.notFound().build());
  // }
}
