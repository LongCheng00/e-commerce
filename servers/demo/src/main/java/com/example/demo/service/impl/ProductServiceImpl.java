
package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.model.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderResponse;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
  // use ConcurrentHashMap store products
  private final ConcurrentHashMap<Long, Product> productDatabase = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public ProductServiceImpl() {
    List<Product> initProducts = List.of(
        new Product(1L, "Product 1", 3.99, 10),
        new Product(2L, "Product 2", 2.99, 10),
        new Product(3L, "Product 3", 4.99, 10));
    initProducts.forEach(p -> productDatabase.put(p.id(), p));
    idGenerator.set(initProducts.size() + 1);
  }

  @Override
  public List<Product> getProducts() {
    // Implementation to fetch products
    return productDatabase.values().stream()
        .toList(); // Convert ConcurrentHashMap values to List
  }

  @Override
  public Optional<Product> getProductById(Long id) {
    // Implementation to fetch a product by ID
    return getProducts().stream()
        .filter(product -> product.id().equals(id))
        .findFirst();
  }

  @Override
  public boolean reduceStock(Long productId, int quantity) {
    var p0 = productDatabase.get(productId);
    return p0 != null && productDatabase.computeIfPresent(productId, (id, product) -> {
      if (product.stock() >= quantity) {
        return new Product(
            product.id(),
            product.name(),
            product.price(),
            product.stock() - quantity);
      }
      return product; // insufficient stock, not modify, return original product
    }).stock() != p0.stock(); // check if stock was reduced
  }

  @Override
  public boolean checkStock(Long productId, int quantity) {
    return Optional.ofNullable(productDatabase.get(productId))
        .map(product -> product.stock() >= quantity)
        .orElse(false);
  }
}