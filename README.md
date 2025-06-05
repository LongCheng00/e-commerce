# e-commerce

A full-stack code challenge

## How to run

### How to run backend

### How to run frontend

### Example API calls

* Call get /products example

```bash
curl --location --request GET 'localhost:8088/products'
```

## Features

\[\]-Backend: Spring Boot3.0+, Java17+, In-memory storage

\[-\]--API GET /products, return a list of products(id,name,price,stock)

\[\]--API POST /orders, create a new order.

\-----Input: product id and purchase quantity.

\-----Check if there's enough stock.

\-----If success, reduce stock.

\-----Return: order ID and total price.

\[\]--API GET /orders/{id} (Optional)

\[\]-Frontend: React, Taro, Typescript

\[\]--Display the product list

\[\]--Allow users to select a product, enter the quantity, and place an order

\[\]--After a successful order, display the order confirmation(order ID,total price)

\[\]-Docs: how to run both backend and frontend.

\[\]-Docs: the example API calls backend apis(curl or Postman)


