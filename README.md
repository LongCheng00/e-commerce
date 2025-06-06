# e-commerce

A full-stack code challenge

## How to run

To running this demo, the **java**, **mvn**, **nodejs** and **pnpm** should be ready in the environment.

* Install jdk17+

  <https://openjdk.org/projects/jdk/17/>
* Install maven

  <https://maven.apache.org/>

### How to run backend

* Run below script in root project dir

  ```bash
  pnpm dev:back
  ```

### How to run frontend

### Example API calls

* Call get /products example

```bash
curl --location --request GET 'localhost:8088/products'
```

* Call post /orders example

```bash
  curl --location --request POST 'localhost:8088/orders' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "items": [
      {"productId": 2, "quantity": 1},
      {"productId": 3, "quantity": 1}
    ]
  }'
```

* Call get /orders/{id} example

  ```bash
  curl --location --request GET 'localhost:8088/orders/1'
  ```

## Features

\[-\]-Backend: Spring Boot3.0+, Java17+, In-memory storage

\[-\]--API GET /products, return a list of products(id,name,price,stock)

\[-\]--API POST /orders, create a new order.

\-----Input: product id and purchase quantity.

\-----Check if there's enough stock.

\-----If success, reduce stock.

\-----Return: order ID and total price.

\[-\]--API GET /orders/{id} (Optional)

\[\]-Frontend: React, Taro, Typescript

\[\]--Display the product list

\[\]--Allow users to select a product, enter the quantity, and place an order

\[\]--After a successful order, display the order confirmation(order ID,total price)

\[\]-Docs: how to run both backend and frontend.

\[\]-Docs: the example API calls backend apis(curl or Postman)


