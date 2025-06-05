package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Product;

public interface ShopService {
  List<Product> getProducts();
  Optional<Product> getProductById(Long id);
}
