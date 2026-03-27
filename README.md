# Backend Engineer Test - Tarto Rizaldi

##  Overview

This project demonstrates a **microservices architecture** consisting of:

* **Payment Service** → Handles payment processing with idempotency (anti double charge)
* **Order Service** → Creates order and communicates with payment service (with retry mechanism)
* **Notification Service** → Simulates event-driven notification

The system is designed to handle:

* Duplicate payment callbacks
* Network failures and retries
* Prevention of double charge
* Basic event-driven flow

---

##  Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database (in-memory)
* REST API (JSON)

---

##  How to Run

### 1. Run the Application

```bash
mvn spring-boot:run
```

Application will run at:

```
http://localhost:8081
```

---

## API Testing

### 1. Create Payment

```
POST /payment/create
```

Request:

```json
{
  "orderId": "123",
  "amount": 10000
}
```

Response:

```json
{
  "orderId": "123",
  "amount": 10000,
  "status": "PENDING"
}
```

---

### 2. Payment Callback (Simulate Gateway)

```
POST /payment/callback
```

Request:

```json
{
  "orderId": "123",
  "status": "SUCCESS"
}
```

---

###  Idempotency Test (IMPORTANT)

Send callback **multiple times**:

Expected result:

```
Already processed
```

 Ensures:

* No duplicate processing
* No double charge

---

###  3. Create Order

```
POST /order
```

Request:

```json
{
  "orderId": "999",
  "amount": 50000
}
```

Flow:

1. Order service is called
2. Calls payment service
3. Payment is created

 Includes:

* Retry mechanism
* Exponential backoff

---

### 4. Notification Service

```
POST /notification/payment-success
```

Request:

```json
{
  "orderId": "999"
}
```

Output (log):

```
Send notification for order: 999
```

---

##  Database Access (H2 Console)

Open in browser:

```
http://localhost:8081/h2-console
```

Configuration:

| Field    | Value                  |
| -------- | ---------------------- |
| JDBC URL | jdbc:h2:mem:payment-db |
| Username | sa                     |
| Password | (empty)                |

Run query:

```sql
SELECT * FROM PAYMENT;
```

---

##  Design Considerations

###  Idempotency

Payment callback is designed to be idempotent:

* Prevents duplicate processing
* Avoids double charge

---

### Retry Mechanism

Order service implements retry with exponential backoff:

* Handles timeout
* Handles temporary failures

---

### Event-Driven Simulation

Payment service emits event (log simulation):

* Represents integration with message broker (Kafka/RabbitMQ)

---

### Database

* H2 in-memory database used for simplicity
* Easily replaceable with PostgreSQL / Oracle

---

## Notes

* This project focuses on **correctness, reliability, and system design**
* Designed to simulate real-world backend scenarios
* Easily extendable to production-grade architecture

---

##  Author

**Tarto Rizaldi**

---
