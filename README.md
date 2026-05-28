<div align="center">

# 🛒 CreatorStore API

### A production-ready RESTful E-Commerce Backend built with Spring Boot

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://supabase.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)

> **CreatorStore** is a clean, layered RESTful API for managing products and customer orders — built for speed, scalability, and developer clarity.

</div>

---

## ✨ Features at a Glance

| Feature | Details |
|---|---|
| **Product Management** | Full CRUD — create, read, update, delete products |
| **Order Processing** | Atomic order creation with stock validation & auto price capture |
| **Inventory Control** | Real-time stock deduction on order confirmation |
| **Input Validation** | Jakarta Bean Validation on all request bodies |
| **API Documentation** | Interactive Swagger UI via SpringDoc OpenAPI |
| **Env-based Config** | Sensitive credentials loaded from `.env` via dotenv-java |
| **Auto Schema** | Hibernate DDL auto-update — zero manual migrations |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    REST Client / Swagger                 │
└───────────────────────┬─────────────────────────────────┘
                        │  HTTP
┌───────────────────────▼─────────────────────────────────┐
│                    Controllers Layer                      │
│         HealthCheck  │  ProductController  │  OrderController  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                    Service Layer                          │
│              ProductService  │  OrderService             │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                  Repository Layer (JPA)                   │
│    ProductRepo  │  OrderRepo  │  OrderItemRepo            │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│               PostgreSQL (Supabase Cloud)                 │
│         products  │  orders  │  order_items               │
└─────────────────────────────────────────────────────────┘
```

---

## 🗂️ Project Structure

```
CreatorStore/
├── src/
│   ├── main/
│   │   ├── java/xyz/therahul/creatorstore/
│   │   │   ├── controllers/
│   │   │   │   ├── HealthCheck.java          # Server health endpoint
│   │   │   │   ├── ProductController.java    # Product CRUD endpoints
│   │   │   │   └── OrderController.java      # Order management endpoints
│   │   │   ├── entities/
│   │   │   │   ├── Product.java              # Product JPA entity
│   │   │   │   ├── Order.java                # Order JPA entity
│   │   │   │   └── OrderItem.java            # Order line-item entity
│   │   │   ├── services/
│   │   │   │   ├── ProductService.java       # Product business logic
│   │   │   │   └── OrderService.java         # Order + stock logic (@Transactional)
│   │   │   ├── repositories/
│   │   │   │   ├── ProductRepository.java    # JpaRepository<Product, Long>
│   │   │   │   ├── OrderRepository.java      # JpaRepository<Order, Long>
│   │   │   │   └── OrderItemRepository.java  # JpaRepository<OrderItem, Long>
│   │   │   ├── dto/
│   │   │   │   ├── OrderRequest.java         # Incoming order payload
│   │   │   │   ├── OrderItemRequest.java     # Individual line-item in order
│   │   │   │   └── HealthCheckResponse.java  # Health check response shape
│   │   │   └── CreatorStoreApplication.java  # Main entry point
│   │   └── resources/
│   │       └── application.yaml             # App + DB configuration
├── .env                                     # Database credentials (gitignored)
├── pom.xml                                  # Maven dependencies
└── README.md
```

---

## 📦 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL via Supabase |
| Build Tool | Apache Maven |
| API Docs | SpringDoc OpenAPI 3 + Swagger UI |
| Code Gen | Lombok |
| Config | YAML + dotenv-java |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5 + Spring Boot Test |

---

## 🛠️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL database (or a [Supabase](https://supabase.com) project)

### 1. Clone the repository

```bash
git clone https://github.com/devMonkRahul/Ecommerce-SpringBoot.git
cd CreatorStore
```

### 2. Configure environment variables

Create a `.env` file in the project root:

```env
DATABASE_URL=jdbc:postgresql://<host>:<port>/<database>
DATABASE_USERNAME=your_db_username
DATABASE_PASSWORD=your_db_password
```

> The app uses **dotenv-java** to auto-load these before Spring initializes — no manual export needed.

### 3. Build and run

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`

---

## 📡 API Endpoints

### Health Check

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Server health status |

### Products — `/api/products`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `PUT` | `/api/products/{id}` | Update product by ID |
| `DELETE` | `/api/products/{id}` | Delete product by ID |

### Orders — `/api/orders`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders` | Get all orders |
| `GET` | `/api/orders/{id}` | Get order by ID |

---

## 🔄 Order Flow

When a `POST /api/orders` request is received, the following happens **atomically** (`@Transactional`):

```
1. Validate all product IDs exist
2. Check stock availability for each item
3. Calculate total price using current product prices
4. Deduct stock quantities from each product
5. Persist OrderItem records with price-at-purchase snapshot
6. Set order status → "CONFIRMED"
7. Return the saved Order object
```

> If any step fails (e.g. insufficient stock), the entire transaction is rolled back — no partial orders.

---

## 📋 Sample Requests

### Create a Product

```json
POST /api/products
{
  "name": "Wireless Headphones",
  "description": "Noise-cancelling over-ear headphones",
  "category": "Electronics",
  "price": 2999.99,
  "stockQuantity": 50
}
```

### Place an Order

```json
POST /api/orders
{
  "customerName": "Rahul Pal",
  "customerEmail": "rahul@example.com",
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

---

## 🗄️ Database Schema

```sql
-- products
CREATE TABLE products (
  id             BIGSERIAL PRIMARY KEY,
  name           VARCHAR NOT NULL,
  description    VARCHAR,
  category       VARCHAR,
  price          DECIMAL NOT NULL,
  stock_quantity INTEGER NOT NULL
);

-- orders
CREATE TABLE orders (
  id             BIGSERIAL PRIMARY KEY,
  customer_name  VARCHAR NOT NULL,
  customer_email VARCHAR NOT NULL,
  status         VARCHAR NOT NULL,
  total_price    DECIMAL NOT NULL,
  created_at     TIMESTAMP
);

-- order_items
CREATE TABLE order_items (
  id                BIGSERIAL PRIMARY KEY,
  quantity          INTEGER NOT NULL,
  price_at_purchase DECIMAL NOT NULL,
  order_id          BIGINT REFERENCES orders(id),
  product_id        BIGINT REFERENCES products(id)
);
```

> Schema is auto-managed by Hibernate (`ddl-auto: update`). No manual migrations needed.

---

## 📖 API Documentation

Once the server is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

Explore and test all endpoints directly from your browser — no Postman required.

---

## 🔒 Validation Rules

| Field | Rule |
|---|---|
| `product.name` | Not blank |
| `product.price` | Must be > 0 |
| `product.stockQuantity` | Must be ≥ 0 |
| `order.customerName` | Not blank |
| `order.customerEmail` | Valid email format |
| `order.items` | Not empty |
| `orderItem.productId` | Not null |
| `orderItem.quantity` | Min value: 1 |

---

## 🚀 Roadmap

- [ ] User authentication & JWT security
- [ ] Role-based access control (Admin / Customer)
- [ ] Product search & filtering
- [ ] Pagination & sorting on list endpoints
- [ ] Order cancellation & refund flow
- [ ] Payment gateway integration

---

## 👨‍💻 Author

**Rahul Pal** — [@devMonkRahul](https://github.com/devMonkRahul)

---

<div align="center">

Made with ❤️ and Spring Boot

</div>
