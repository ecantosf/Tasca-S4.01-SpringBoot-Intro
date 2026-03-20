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

This project is a robust **REST API** for user management, developed using **Spring Boot 3** as part of the IT Academy Java Bootcamp. The main objective is to learn and apply the fundamentals of Spring Boot, REST API development, and layered architecture.

The project is structured in three progressive levels:

- **Level 1:** Basic API setup with health check endpoint, JSON responses, testing with MockMvc, and JAR packaging.
- **Level 2:** Complete CRUD operations for user management with in-memory persistence.
- **Level 3:** Advanced testing, error handling, and architectural refinements.

---

## 🏗️ Project Architecture

Following **SOLID** principles and **Layered Architecture**, the application is divided into clear responsibilities to facilitate maintenance and scalability.

### Directory Hierarchy

```text
userapi
├── src/main/java/cat/itacademy/s04/t01/userapi
│   ├── UserapiApplication.java   # Main application entry point
│   ├── controller/               # REST endpoints (Health & User)
│   ├── service/                  # Business logic layer
│   ├── repository/               # Data access layer (in-memory)
│   ├── model/                    # Domain models (User, HealthResponse)
│   └── exception/                # Custom exceptions (prepared for Level 3)
├── src/test/java/                # Full test suite
│   └── controller/               # Web layer tests with MockMvc
├── src/main/resources/
│   └── application.properties    # Configuration (server.port=9000)
└── target/                       # Compiled artifacts (.jar)
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
