<div align="center">

# 👤 S04.T01 - User Management API

**Developed by:**
[Eduard Cantos](https://github.com/ecantosf)

*(IT Academy Java Bootcamp - Spring Boot Fundamentals)*

---

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white)
</div>

---

## 📖 Introduction

This project is a robust **REST API** for user management, developed using **Spring Boot 3**. The main objective was to apply a strict **Layered Architecture**, ensuring decoupling between business logic, service exposure, and in-memory data persistence.

The application has been validated through a comprehensive testing strategy, combining unit, integration, and web layer tests to guarantee endpoint stability.

---

## 🏗️ Project Architecture

Following **SOLID** principles, the application is divided into clear responsibilities to facilitate maintenance and scalability.

### Directory Hierarchy
```text
userapi
├── src/main/java/cat/itacademy/s04/t01/userapi
│   ├── controller       # REST Endpoints (Health & User)
│   ├── service          # Service Layer (Business Logic)
│   ├── repository       # Persistence Layer (InMemory)
│   ├── entity           # Domain Models (User)
│   ├── dto              # Transfer Objects (HealthStatus)
│   └── exception        # Custom Exceptions (404, 409)
├── src/test/java        # Full Test Suite
└── target               # Compiled Artifacts (.jar)
```
---

## 🚀 Key Features

### 🛠️ User Management
* **Unique Email Validation**: Implemented in the service layer to prevent duplicate registrations.
* **ID Generation**: Centralized use of `UUID` managed by the service before persistence.
* **Advanced Search**: User filtering by name with support for partial matches and *case-insensitive* search.
* **Health Check**: Structured endpoint at `/health` for basic system monitoring.

### ⚠️ Global Error Handling
Use of custom exceptions annotated with `@ResponseStatus`:
* `NotFoundByIdException` -> **404 Not Found**
* `EmailAlreadyExistsException` -> **409 Conflict**

---

## 🛠️ Patterns and Technologies

To ensure software quality, the following concepts have been applied:

1. **Inversion of Control (IoC)**: Dependency injection via constructor.
2. **Repository Abstraction**: The service depends on an interface, allowing migration from an in-memory list to a real database without logic changes.
3. **Clean Code**: Descriptive naming, short methods, and strict separation of concerns.

| Technology | Usage |
| :--- | :--- |
| **Java 21** | Main language and business logic |
| **Spring Web** | Creation of REST controllers |
| **Mockito** | Dependency mocking in unit tests |
| **Jackson** | Automatic JSON serialization/deserialization |

---

## 🧪 Testing Strategy

Maximum code coverage was achieved through:

* **Unit Tests**: Isolated validation of `UserService` logic using Mocks.
* **Web Layer Tests**: Controller testing using `@WebMvcTest`.
* **Integration Tests**: End-to-end validation with `@SpringBootTest` and `MockMvc`.

To run the tests:
```bash
mvn test
```
## 🚦 Installation and Execution

1. **Clone the repository and compile:**
```bash
mvn clean package
```
2. **Run the generated file:**
```bash
java -jar target/userapi-0.0.1-SNAPSHOT.jar
```
3. **Main Endpoints:**
* **GET /health** - Check service status.
* **POST /users** - Create a user (JSON body).
* **GET /users?name=ana** - Filter users.

---

**Final Note**: This project serves as a foundation for a scalable architecture, ready to integrate persistent databases such as MySQL or MongoDB.
