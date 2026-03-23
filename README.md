# ☕ Coffee Shop API

A Spring Boot backend application for managing coffee orders, enhanced with validation, external service integration, and a hybrid database architecture using MySQL and MongoDB.
---

## 📌 Features

* Create and manage coffee orders
* Input validation using Jakarta Validation
* External pricing service integration via WebClient
* Hybrid database architecture:

  * MySQL for transactional data
  * MongoDB for logging (order creation logs)
* RESTful API design following best practices
* Global exception handling
* Lombok integration to reduce boilerplate code
* Unit & integration testing using JUnit and MockServer
---

## 🛠 Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA (MySQL)
* Spring Data MongoDB
* Spring WebFlux (WebClient)
* Lombok
* JUnit 5
* MockServer
* Maven

---

## 🗄 Database Design

### 🔹 MySQL (Primary Database)

* Stores core business data:

  * Orders
  * Coffee details
* Ensures data consistency and relationships

### 🔹 MongoDB (Logging Database)

* Stores order logs (e.g., `order_logs` collection)
* Captures:

  * Order creation events
  * Request/response details (optional)
* Optimized for flexible and scalable logging

---

## 🔄 API Workflow

1. Client sends an order request
2. Request is validated
3. Application calls external pricing service via WebClient
4. Total price is calculated
5. Order is saved in MySQL
6. Order log is stored in MongoDB
7. Response is returned to the client

---

## 🏗 Project Structure

```
src/main/java/com/jjcoffee/coffee_shop_api
│
├── controller     # REST Controllers
├── service        # Business logic
├── repository     # JPA & MongoDB repositories
├── entity         # JPA entities (MySQL)
├── mongo          # MongoDB documents (logs)
├── dto            # Request & Response models
├── config         # Configuration classes
└── exception      # Global exception handling
```

---

## 🧪 Testing

* Unit and integration testing using **JUnit 5**
* External pricing service is mocked using **MockServer**
* Ensures:

  * Reliable service logic
  * Isolation from external dependencies

---

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/jjlong-new/coffee-shop-api.git
cd coffee-shop-api
```

### 2. Configure databases

#### MySQL

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/coffee_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### MongoDB
```
spring.data.mongodb.uri=mongodb://localhost:27017/coffee_db
```

### 3. Run the application
```bash
mvn spring-boot:run
---

## 4. Access the API
```
http://localhost:8080

---

## 📡 Sample Endpoints

| Method | Endpoint     | Description     |
| ------ | ------------ | --------------- |
| POST   | /orders      | Create an order |
| GET    | /orders/{id} | Get order by ID |
---

## 👨‍💻 Author

Joseph Long
---
