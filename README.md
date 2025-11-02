Medical Store Management System - README

# Medical Store Management System

## 1. Overview

The **Medical Store Management System** is a secure and scalable web application designed to automate daily operations of a medical store — including managing medicines, categories, suppliers, customers, orders, and inventory. It is built using **Spring Boot (MVC)**, **Thymeleaf**, and **PostgreSQL**, providing a modular, maintainable, and user-role-driven system for administrators, suppliers, and customers.

---

## 2. Objective

The primary objective of this project is to develop a **robust, secure, and modular medical store system** using Java and Spring Boot, with complete backend-driven UI (no JavaScript) and seamless integration between all modules using dependency injection.

---

## 3. Technology Stack

| Layer | Technology | Description |
|-------|-------------|-------------|
| Frontend (View) | Thymeleaf + Tailwind CSS | Responsive, server-rendered HTML views |
| Backend (Controller & Service) | Spring Boot (Spring Web, Validation, Security, JDBC) | Core application logic and routing |
| Database | PostgreSQL | Relational data management |
| Security | Spring Security + BCrypt | Role-based authentication and password hashing |
| Build Tool | Maven | Dependency management |
| IDE | IntelliJ IDEA / STS | Development environment |
| Version Control | GitHub | Source code management |

---

## 4. Project Structure

```
medicalstore/
 ├── controller/        → Web controllers (handle HTTP requests)
 ├── service/           → Business logic layer
 ├── repository/        → JDBC data access layer
 ├── model/             → Entity classes (POJOs)
 ├── config/            → Security & database configurations
 ├── templates/         → Thymeleaf HTML pages
 ├── static/            → Tailwind CSS assets
 └── resources/
     ├── application.properties
     └── schema.sql
```

---

## 5. User Roles

| Role | Description | Permissions |
|------|--------------|--------------|
| Admin | Manages the entire system | Full CRUD on all modules |
| Supplier | Manages medicines and stock | Add/update medicines, view inventory |
| Customer | End user | Browse medicines, place orders |

---

## 6. Database Schema (PostgreSQL)

### Tables

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(150),
    address TEXT
);

CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE medicines (
    medicine_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category_id INT REFERENCES categories(category_id) ON DELETE SET NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    supplier_id INT REFERENCES users(user_id) ON DELETE SET NULL,
    expiry_date DATE
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    total_amount DECIMAL(10,2),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PENDING'
);

CREATE TABLE order_items (
    item_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(order_id) ON DELETE CASCADE,
    medicine_id INT REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    supplier_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    medicine_id INT REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 7. How to Run the Project

### Step 1: Prerequisites
- Install **Java JDK 17+**
- Install **Maven**
- Install **PostgreSQL**
- Set up **IntelliJ IDEA** or **Spring Tool Suite (STS)**

### Step 2: Database Setup
1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE medicalstore;
   ```
2. Update the database configuration in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/medicalstore
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.datasource.driver-class-name=org.postgresql.Driver
   ```

### Step 3: Build the Project
In the project root directory, run:
```bash
mvn clean install
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```
or run directly from your IDE using the main class:
```
com.medicalstore.MedicalStoreApplication
```

### Step 5: Access in Browser
After successful startup, open:
```
http://localhost:8080/
```

---

## 8. Default Module URLs

| Module | URL | Method | Description |
|---------|-----|---------|-------------|
| Login | `/login` | GET | Login page |
| Register | `/register` | GET/POST | User registration |
| Medicine List | `/medicine` | GET | View medicines |
| Add Medicine | `/medicine/add` | POST | Add new medicine |
| Categories | `/categories` | GET | View categories |
| Orders | `/orders` | GET | View all orders |
| Inventory | `/inventory` | GET | View stock and update |

---

## 9. Security Configuration

- Passwords are stored using **BCryptPasswordEncoder**.
- Role-based access is enforced via **Spring Security annotations** such as:
  ```java
  @PreAuthorize("hasRole('ADMIN')")
  ```
- CSRF protection is enabled by default.

---

## 10. Future Enhancements

- Email notifications for order updates
- PDF invoice export
- Expiry alerts for medicines
- Cloud deployment (AWS / Render)
- Integration with hospital/pharmacy APIs

---

## 11. Credits

Developed by a 5-member team as part of a Hackathon/Academic project.  
Each member contributed to a specific module including Backend, Database, UI, Security, Inventory, and Reporting.

