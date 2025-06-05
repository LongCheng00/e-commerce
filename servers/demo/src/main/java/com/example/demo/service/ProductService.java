package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.OrderResponse;
import com.example.demo.model.Product;

public interface ProductService {
  List<Product> getProducts();

  Optional<Product> getProductById(Long id);

  boolean reduceStock(Long productId, int quantity);

  boolean checkStock(Long productId, int quantity);
}
