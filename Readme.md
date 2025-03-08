# User Service - Twitter Microservices

## 📌 Introduction
The **User Service** is a microservice component of the `twitter-spring-reactjs` project, responsible for user management, including authentication, profile management, and user-related operations.

## 🏗️ Architecture & Technologies
- **Spring Boot 3.x** - Backend framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database access
- **PostgreSQL** - Relational database
- **Kafka** - Event-driven communication
- **Redis** - Caching layer
- **Lombok** - Simplified Java development
- **MapStruct** - Object mapping
- **Docker** - Containerization
- **Swagger** - API documentation

## 📂 Project Structure
```
user-service/
│── src/
│   ├── main/java/com/twitter/userservice/
│   │   ├── controller/  # API endpoints
│   │   ├── service/      # Business logic
│   │   ├── repository/   # Database access
│   │   ├── model/        # Entity models
│   │   ├── config/       # Configuration classes
│   │   ├── security/     # Authentication & authorization
│   ├── resources/
│   │   ├── application.yml  # Spring Boot configuration
│   ├── test/java/com/twitter/userservice/  # Unit & Integration tests
│── Dockerfile
│── pom.xml  # Maven dependencies
```

## 🚀 Getting Started

### Prerequisites
Ensure you have installed:
- **JDK 17+**
- **Maven**
- **Docker** (for containerization)
- **PostgreSQL** (if running locally)

### Installation & Setup
1. **Clone the repository**
```sh
 git clone https://github.com/merikbest/twitter-spring-reactjs.git
 cd twitter-spring-reactjs/user-service
```

2. **Configure Database**
   Update `application.yml` with your PostgreSQL credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/twitter_db
    username: your_username
    password: your_password
```

3. **Run the service**
```sh
mvn clean install
mvn spring-boot:run
```

4. **Access API documentation (Swagger UI)**
```
http://localhost:8081/swagger-ui.html
```

## 🎯 Key Features
✅ User Registration & Login (JWT authentication)  
✅ Profile Management (update details, change password, etc.)  
✅ User Search & Recommendations  
✅ Event-driven communication using Kafka  
✅ Caching with Redis for performance optimization

## 📡 API Endpoints (Sample)
| Method | Endpoint               | Description           |
|--------|------------------------|-----------------------|
| POST   | `/api/auth/register`   | Register new user    |
| POST   | `/api/auth/login`      | Authenticate user    |
| GET    | `/api/users/{id}`      | Get user profile     |
| PUT    | `/api/users/{id}`      | Update user profile  |
| DELETE | `/api/users/{id}`      | Delete user account  |

## 🛠️ Running with Docker
To run the User Service in a Docker container:
```sh
docker build -t user-service .
docker run -p 8081:8081 user-service
```

## 🏗️ Microservices Integration
This service communicates with:
- **Auth Service** for user authentication
- **Tweet Service** for handling user tweets
- **Notification Service** via Kafka events

## 🧪 Testing
Run unit and integration tests:
```sh
mvn test
```

## 📜 License
This project is licensed under the MIT License.

---
This README provides an overview of the **User Service**. For additional details, refer to the full repository documentation.

