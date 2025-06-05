
package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Product(
    Long id,
    String name,
    @JsonFormat(shape = JsonFormat.Shape.STRING) double price,
    int stock
) {
    // 验证逻辑可以使用紧凑构造函数
    public Product {
        if (price < 0) {
            throw new IllegalArgumentException("价格不能为负数");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("库存不能为负数");
        }
    }
}
