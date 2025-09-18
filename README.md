# 🚦 Rate Limiter Service

A scalable **API Rate Limiter microservice** built with **Java + Spring Boot** that supports multiple rate limiting algorithms.  
It uses **Redis** for distributed request tracking, is fully containerized with **Docker**, and comes with **Swagger API documentation** for easy testing.

---

## ✨ Features
- 🔹 **Multiple algorithms supported**
  - Fixed Window
  - Sliding Window Log
  - Sliding Window Counter
  - Token Bucket
  - Leaky Bucket
- 🔹 **Redis backend** for distributed and scalable request tracking
- 🔹 **Configuration-based strategy selection** (switch algorithms without code changes)
- 🔹 **Dockerized** for easy deployment
- 🔹 **Swagger UI** for API exploration and testing
- 🔹 **REST APIs** ready for integration

---

## 🛠️ Tech Stack
- **Java 21**
- **Spring Boot 3**
- **Redis**
- **Docker / Docker Compose**
- **Swagger / OpenAPI**
- **Maven**

---

## 🚀 Getting Started

### 1️⃣ Clone the repository
### 2️⃣ Build the project
  ``` mvn clean package -DskipTests ```
### 3️⃣ Run with docker compose
  ``` docker-compose up --build```

### This will:
  - Start a Redis container
  - Start the Rate Limiter container (Spring Boot app)

