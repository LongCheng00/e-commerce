
package com.example.demo.test.service;

import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.OrderResponse;
import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  private OrderServiceImpl orderService;

  private CreateOrderRequest validRequest;
  private CreateOrderRequest insufficientStockRequest;
  private CreateOrderRequest invalidProductRequest;

  @BeforeEach
  void setUp() {
    // 准备测试数据
    validRequest = new CreateOrderRequest(List.of(
        new CreateOrderRequest.OrderItemRequest(1L, 2),
        new CreateOrderRequest.OrderItemRequest(3L, 1)));

    insufficientStockRequest = new CreateOrderRequest(List.of(
        new CreateOrderRequest.OrderItemRequest(1L, 100)));

    invalidProductRequest = new CreateOrderRequest(List.of(
        new CreateOrderRequest.OrderItemRequest(99L, 1)));

    // 配置模拟行为
    lenient().when(productService.checkStock(1L, 2)).thenReturn(true);
    lenient().when(productService.checkStock(1L, 100)).thenReturn(false);
    lenient().when(productService.checkStock(3L, 1)).thenReturn(true);
    lenient().when(productService.checkStock(99L, 1)).thenReturn(false);

    lenient().when(productService.getProductById(1L))
        .thenReturn(Optional.of(new Product(1L, "Product 1", 3.99, 10)));
    lenient().when(productService.getProductById(3L))
        .thenReturn(Optional.of(new Product(3L, "Product 3", 4.99, 10)));
    lenient().when(productService.getProductById(99L))
        .thenReturn(Optional.empty());

    lenient().when(productService.reduceStock(anyLong(), anyInt()))
        .thenAnswer(invocation -> {
          Long productId = invocation.getArgument(0);
          int quantity = invocation.getArgument(1);
          return productId != 99L && quantity <= 50;
        });
  }

  @Test
  void createOrder_shouldSucceedWithValidRequest() {
    // When
    OrderResponse response = orderService.createOrder(validRequest);

    // Then
    assertNotNull(response);
    assertEquals(1L, response.orderId());
    assertEquals(3.99 * 2 + 4.99, response.totalPrice(), 0.001);

    // 验证库存扣减被调用
    verify(productService, times(1)).reduceStock(1L, 2);
    verify(productService, times(1)).reduceStock(3L, 1);
  }

  @Test
  void createOrder_shouldThrowWhenInsufficientStock() {
    // When & Then

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrder(insufficientStockRequest));

    assertEquals("Insufficient stock: Product 1 (Stock: 10, Required: 100)", exception.getMessage());

    // 验证库存扣减未被调用
    verify(productService, never()).reduceStock(anyLong(), anyInt());
  }

  @Test
  void createOrder_shouldThrowWhenProductNotFound() {
    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrder(invalidProductRequest));

    assertEquals("Product does not exist: 99", exception.getMessage());

    // 验证库存扣减未被调用
    verify(productService, never()).reduceStock(anyLong(), anyInt());
  }

  @Test
  void createOrder_shouldRollbackWhenPartialFailure() {
    // 配置一个部分失败的场景
    when(productService.checkStock(1L, 2)).thenReturn(true);
    when(productService.checkStock(3L, 100)).thenReturn(false);

    CreateOrderRequest request = new CreateOrderRequest(List.of(
        new CreateOrderRequest.OrderItemRequest(1L, 2),
        new CreateOrderRequest.OrderItemRequest(3L, 100)));

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrder(request));

    assertEquals("Insufficient stock: Product 3 (Stock: 10, Required: 100)", exception.getMessage());

    // 验证第一个产品的库存扣减被回滚（未被调用）
    verify(productService, never()).reduceStock(anyLong(), anyInt());
  }
}