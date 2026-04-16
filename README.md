## Microservices example (Java 21, Spring Boot, Postgres)

This repo is a simple microservices example with:

- `product-service` (port `8082`) backed by Postgres database `products_db`
- `order-service` (port `8081`) backed by Postgres database `orders_db`

`order-service` uses **`RestTemplate`** to call `product-service` when creating an order.

### Run Postgres

```bash
docker compose up -d
```

### Run the services

In separate terminals:

```bash
./mvnw -pl product-service spring-boot:run
```

```bash
./mvnw -pl order-service spring-boot:run
```

### Try it

Create a product:

```bash
curl -X POST http://localhost:8082/api/products ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Keyboard\",\"sku\":\"KB-001\",\"price\":49.99,\"availableQuantity\":100}"
```

List products:

```bash
curl http://localhost:8082/api/products
```

Create an order (calls product-service):

```bash
curl -X POST http://localhost:8081/api/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"productId\":1,\"quantity\":2}"
```

List orders:

```bash
curl http://localhost:8081/api/orders
```

