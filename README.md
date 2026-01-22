# E-Commerce Microservices

A backend system for an e-commerce platform built with microservices. The interesting part? It uses both synchronous REST calls and asynchronous Kafka events — because real systems need both.

## Why I Built It This Way

Most tutorial projects either go full REST or full event-driven. But in production, you usually need a mix:

- **REST** for things that need an immediate answer ("does this user have enough balance?")
- **Kafka** for things that can happen in the background ("deduct the balance, update inventory")

So when a user places an order:
1. We validate stock and balance synchronously (can't accept an order we can't fulfill)
2. We save the order as `PENDING` and return immediately
3. Kafka events trigger the actual balance deduction and inventory update async

The user gets a fast response. The heavy lifting happens in the background.

## Architecture

```
                                    ┌─────────────────┐
                                    │   API Gateway   │
                                    │    (port 8765)  │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
                    ▼                        ▼                        ▼
           ┌───────────────┐       ┌─────────────────┐       ┌───────────────┐
           │ User Service  │       │  Order Service  │       │Product Service│
           │  (port 8081)  │       │   (port 8082)   │       │  (port 8083)  │
           └───────────────┘       └────────┬────────┘       └───────────────┘
                    ▲                       │                        ▲
                    │                       │ publish                │
                    │                       ▼                        │
                    │              ┌─────────────────┐               │
                    │              │  Kafka Topics   │               │
                    │              │ "order-events"  │               │
                    │              └────────┬────────┘               │
                    │                       │                        │
                    └───────────── consume ─┴─ consume ──────────────┘
```

## Services

| Service | What it does |
|---------|--------------|
| **api-gateway** | Single entry point. Routes requests, handles JWT validation |
| **service-registry** | Eureka server. Services register here so they can find each other |
| **user-service** | User accounts, authentication, balance management |
| **order-service** | Creates orders, publishes events to Kafka |
| **products-service** | Product catalog, inventory management |

## Tech Stack

- **Java 21 + Spring Boot 4**
- **Spring Cloud** (Gateway, Eureka)
- **Apache Kafka** for async messaging
- **PostgreSQL**
- **JWT** for auth
- **Docker Compose** for Kafka/Zookeeper

## Running Locally

### 1. Start Kafka

```bash
docker-compose up -d
```

This spins up Zookeeper, Kafka, and a nice UI at http://localhost:8080 to see your topics.

### 2. Start the services (in order)

```bash
# Terminal 1 - Service Registry (start this first, others need it)
cd service-registry && ./mvnw spring-boot:run

# Terminal 2 - User Service
cd user-service && ./mvnw spring-boot:run

# Terminal 3 - Products Service
cd products-service && ./mvnw spring-boot:run

# Terminal 4 - Order Service
cd order-service && ./mvnw spring-boot:run

# Terminal 5 - API Gateway (start last)
cd api-gateway && ./mvnw spring-boot:run
```

### 3. Test it

Register a user:
```bash
curl -X POST http://localhost:8765/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email: "john@example.com" ,"username": "john", "password": "password123", "balance": 1000}'
```

Login and grab the token:
```bash
curl -X POST http://localhost:8765/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "password123"}'
```

Place an order:
```bash
curl -X POST http://localhost:8765/orders \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{"item_name": "laptop", "category": "electronics", "quantity": 1}'
```

## The Event Flow

When you place an order, here's what happens:

```
1. POST /orders
   │
2. Order Service validates:
   ├── GET /products/{name}     → "Do we have stock?"
   ├── GET /auth/balance        → "Can user afford it?"
   │
3. If valid:
   ├── Save order (status: PENDING)
   ├── Publish OrderCreatedEvent to Kafka
   └── Return 200 to user (fast!)

4. Async (via Kafka):
   ├── Products Service → reduces inventory
   └── User Service → deducts balance
```

## Design Decisions

**Why not full Kafka?**
We need to validate before accepting an order. Can't tell a user "order placed!" then later say "actually, out of stock." The sync checks upfront prevent that.

**Why not full REST?**
Balance deduction and inventory updates don't need to block the user. Kafka gives us reliability (events persist if a service is down) and decoupling (order service doesn't care how balance is deducted).

**Why Eureka?**
Services can scale up/down and find each other dynamically. The API gateway doesn't hardcode service URLs.

## Project Structure

```
E-CommerceMicroservice/
├── api-gateway/          # Routes & JWT validation
├── service-registry/     # Eureka server
├── user-service/         # Auth & balance
├── order-service/        # Order creation & Kafka producer
├── products-service/     # Catalog & inventory
└── docker-compose.yml    # Kafka + Zookeeper + UI
```
