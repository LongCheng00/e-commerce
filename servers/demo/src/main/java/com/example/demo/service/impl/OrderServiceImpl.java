package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.service.ProductService;
import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderResponse;
import com.example.demo.model.Product;
import com.example.demo.service.OrderService;

import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

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
          .orElseThrow(() -> new IllegalArgumentException("Product does not exist: " + item.productId()));

      if (!productService.checkStock(item.productId(), item.quantity())) {
        throw new IllegalArgumentException(
            "Insufficient stock: " + product.name() +
                " (Stock: " + product.stock() +
                ", Required: " + item.quantity() + ")");
      }
    }
  }

  @Override
  public List<Order> getAllOrders() {
    return new ArrayList<>(orderDatabase.values());
  }

  @Override
  public Optional<Order> getOrderById(Long orderId) { 
    return Optional.ofNullable(orderDatabase.get(orderId));
  }
}