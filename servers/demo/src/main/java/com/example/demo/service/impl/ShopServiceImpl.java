
package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.service.ShopService;
import com.example.demo.model.Product;

@Service
public class ShopServiceImpl implements ShopService {

  private static final List<Product> PRODUCTS = List.of(
      new Product(1L, "Product 1", 3.99, 10),
      new Product(2L, "Product 2", 2.99, 10),
      new Product(3L, "Product 3", 4.99, 10));

  @Override
  public List<Product> getProducts() {
    // Implementation to fetch products
    return PRODUCTS;
  }

  @Override
  public Optional<Product> getProductById(Long id) {
    // Implementation to fetch a product by ID
    return getProducts().stream()
        .filter(product -> product.id().equals(id))
        .findFirst();
  }

}