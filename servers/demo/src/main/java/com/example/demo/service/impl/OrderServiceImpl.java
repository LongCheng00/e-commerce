package com.example.demo.service;

import com.example.demo.model.*;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderServiceImpl implements OrderService {

  private final ProductService productService;
  private final AtomicLong orderIdGenerator = new AtomicLong(1);

  // 模拟订单存储
  private final ConcurrentHashMap<Long, Order> orderDatabase = new ConcurrentHashMap<>();

  public OrderServiceImpl(ProductService productService) {
    this.productService = productService;
  }

  @Override
  // @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    // 1. validate order items
    validateStock(request.items());

    // 2. create order and calculate total price
    List<OrderItem> orderItems = new ArrayList<>();
    double totalPrice = 0.0;

    for (CreateOrderRequest.OrderItemRequest item : request.items()) {
      Product product = productService.getProductById(item.productId())
          .orElseThrow(() -> new IllegalArgumentException("Product does not exist: " + item.productId()));

      // 3. Reduce stock        
      if (!productService.reduceStock(item.productId(), item.quantity())) {
        throw new IllegalStateException("Insufficient stock: " + product.name());
      }

      // 4. create order item
      OrderItem orderItem = new OrderItem(
          item.productId(),
          item.quantity(),
          product.price());

      orderItems.add(orderItem);
      totalPrice += product.price() * item.quantity();
    }

    // 5. create order
    Long orderId = orderIdGenerator.getAndIncrement();
    Order order = new Order(
        orderId,
        List.copyOf(orderItems),  
        totalPrice,
        LocalDateTime.now());

    // save order
    orderDatabase.put(orderId, order);

    return new OrderResponse(orderId, totalPrice);
  }

  private void validateStock(List<CreateOrderRequest.OrderItemRequest> items) {
    for (CreateOrderRequest.OrderItemRequest item : items) {
      Product product = productService.getProductById(item.productId())
          .orElseThrow(() -> new IllegalArgumentException("产品不存在: " + item.productId()));

      if (!productService.checkStock(item.productId(), item.quantity())) {
        System.out.println("检查库存: " + product.name() +
            " (当前库存: " + product.stock() +
            ", 需求: " + item.quantity() + ")");
        throw new IllegalArgumentException(
            "库存不足: " + product.name() +
                " (当前库存: " + product.stock() +
                ", 需求: " + item.quantity() + ")");
      }
    }
  }
}