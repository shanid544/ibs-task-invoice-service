# 💳 Invoice Management System

A Spring Boot-based microservices system for managing users, invoices, items, and sending email notifications with resilience and dynamic configuration.

---

## 🚀 Project Overview

This system includes the following microservices:

### 1. Invoice Service
- Handles user registration and authentication.
- Manages invoices and billing lines.
- Calculates taxes based on country codes using the Strategy pattern.
- Communicates with:
    - **Item Service** using `RestTemplate` to fetch item details.
    - **Notification Service** using `WebClient` to send invoice emails.
- Supports retry and fallback using **Resilience4j**.
- Uses **in-memory cache** if Item Service is unavailable.
- Configurable email version toggle (plain text / PDF) via properties.
- Integrated with **Flyway**, **JWT**, **Spring Security**, and **Swagger**.

### 2. Item Service
- Stores and manages item details.
- Used by the Invoice Service to retrieve item information.
- Uses **H2 Database**.

### 3. Notification Service
- Sends invoice notifications via email to buyers.
- Supports:
    - Plain text emails.
    - PDF attachment emails (generated using iText).
- Email version is passed from Invoice Service as a toggle (`v1` or `v2`).

---

## 🛠️ Features

### ✅ User Module
- Register as `BUYER` or `SUPPLIER`.
- Secure login with JWT token generation.
- Role-based access:
    - `SUPPLIER` can create/delete invoices.
    - `BUYER` and `SUPPLIER` can view and filter invoices.

### 📦 Invoice Module
- Create invoices with billing lines (item IDs).
- Fetch item prices from Item Service.
- Fallback to in-memory cache if Item Service is down.
- Tax calculation per country using Strategy pattern.
- Notify buyer after invoice creation via Notification Service.

### 📬 Notification Module
- Sends invoice email using versioned strategy:
    - `v1`: Plain Text.
    - `v2`: PDF Attachment (iText).
- Uses WebClient with retry and graceful degradation.

---

## 🧪 Quality & Testing

- Unit tests for services, controllers, configs, and exceptions.
- Global exception handler with 100% test coverage.
- Integrated **SonarQube** for code quality analysis.
- Test DB: **H2** (data files committed to Git for reuse).

---

## 🧱 Technologies Used

- Java 21
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- H2 Database
- Flyway
- Swagger / OpenAPI
- Resilience4j
- WebClient / RestTemplate
- iText (PDF generation)
- Thymeleaf (Email templates)
- SonarQube
- JUnit & Mockito

---

## 📁 Setup Instructions

### 📦 Clone Repositories

```bash
git clone https://github.com/shanid544/ibs-task-notification-service
git clone https://github.com/shanid544/ibs-task-item-service
git clone https://github.com/shanid544/ibs-task-invoice-service

## ✅ Prerequisites

- Java 21  
- Maven  
- IntelliJ IDEA or any preferred IDE  
- Postman (for testing APIs)  
- SonarQube (optional, for code analysis)

---

## 🔄 Run Services

| Service      | Port | Run Command           |
|--------------|------|------------------------|
| Invoice      | 9090 | `mvn spring-boot:run`  |
| Item         | 9091 | `mvn spring-boot:run`  |
| Notification | 9093 | `mvn spring-boot:run`  |

> ✅ **H2 data files (`*.mv.db`) are already committed**, so no need to reinitialize the database manually.
