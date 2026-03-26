☕ Coffee Shop API
A Spring Boot backend application for managing coffee orders, enhanced with validation, external service integration, and a hybrid database architecture using MySQL and MongoDB.

---

# 📌 Features

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

### ⚙️ Spring Batch (NEW)

* Export coffee sales data to CSV
* REST-triggered batch job
* Filters data by:

  * `storeId`
  * `date range`
* Applies business logic:

  * Calculates total price
  * Applies discount (`quantity >= 3`)
  * Normalizes beverage names
* Skips invalid records (fault-tolerant processing)
* Logs execution metrics (read, write, skip counts)

---

# 🛠 Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA (MySQL)
* Spring Data MongoDB
* Spring WebFlux (WebClient)
* Spring Batch
* Lombok
* JUnit 5
* MockServer
* Maven

---

# 🗄 Database Design

### 🔹 MySQL (Primary Database)

Stores core business data:

* Orders
* Coffee details
* Batch processing data (`coffee_orders` table)

Ensures data consistency and relationships

---

### 🔹 MongoDB (Logging Database)

Stores order logs (e.g., `order_logs` collection)

Captures:

* Order creation events
* Request/response details (optional)

Optimized for flexible and scalable logging

---

# 🔄 API Workflow

1. Client sends an order request
2. Request is validated
3. Application calls external pricing service via WebClient
4. Total price is calculated
5. Order is saved in MySQL
6. Order log is stored in MongoDB
7. Response is returned to the client

---

# 🔄 Spring Batch Workflow

1. Client triggers batch job via REST API
2. JobParameters are created (`startDate`, `endDate`, `storeId`, `outputFilePath`)
3. Reader fetches filtered data from `coffee_orders`
4. Processor:

   * Validates records
   * Skips invalid records
   * Applies business logic
5. Writer exports data into CSV file
6. Listener logs execution details

---

# 📡 Batch Endpoint

### Export Coffee Sales

```http
POST /orders/batch/export-coffee-sales
```

### Request Body

```json
{
  "startDate": "2024-10-01",
  "endDate": "2024-10-31",
  "storeId": 1,
  "outputFilePath": "C:/temp/output.csv"
}
```

---

# 📄 Sample CSV Output

```csv
orderId,storeId,customerId,beverageName,size,quantity,unitPrice,totalPrice,discountApplied,orderTimestamp
1,1,101,Iced Latte,LARGE,2,150.00,300.00,false,2024-10-10T10:15:00
2,1,102,Hot Americano,MEDIUM,3,120.00,360.00,true,2024-10-11T09:30:00
```

---

# 🏗 Project Structure

```
src/main/java/com/jjcoffee/coffee_shop_api
│
├── controller     # REST Controllers
├── service        # Business logic
├── repository     # JPA & MongoDB repositories
├── entity         # JPA entities (MySQL)
├── mongo          # MongoDB documents (logs)
├── batch          # Spring Batch (reader, processor, writer, config)
├── dto            # Request & Response models
├── config         # Configuration classes
└── exception      # Global exception handling
```

---

# 🧪 Testing

* Unit and integration testing using JUnit 5
* External pricing service is mocked using MockServer

Ensures:

* Reliable service logic
* Isolation from external dependencies

---

# ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/jjlong-new/coffee-shop-api.git
cd coffee-shop-api
```

---

### 2. Configure databases

#### MySQL

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/coffee_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### MongoDB

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/coffee_db
```

---

### 3. Run the application

```bash
mvn spring-boot:run
```

---

### 4. Access the API

```
http://localhost:8080
```

---

# 📡 Sample Endpoints

| Method | Endpoint                          | Description             |
| ------ | --------------------------------- | ----------------------- |
| POST   | /orders                           | Create an order         |
| GET    | /orders/{id}                      | Get order by ID         |
| POST   | /orders/batch/export-coffee-sales | Export coffee sales CSV |

---

# 👨‍💻 Author

Joseph Long
