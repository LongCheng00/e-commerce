package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Product;
import com.example.demo.service.ShopService;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ShopService shopService;

  public ProductController(ShopService shopService) {
    this.shopService = shopService;
  }

  @GetMapping
  public List<Product> getProducts() {
    return shopService.getProducts();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    return shopService.getProductById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
