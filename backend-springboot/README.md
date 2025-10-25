# DubleFin Banking - Spring Boot Microservices

A comprehensive, enterprise-grade online banking application built with Spring Boot microservices architecture following industry best practices.

## 🏗️ Architecture Overview

This application follows a microservices architecture with the following components:

### Infrastructure Services
- **Service Discovery (Eureka Server)** - Service registration and discovery
- **Config Server** - Centralized configuration management
- **API Gateway** - Single entry point with routing, load balancing, and security

### Business Services
- **Auth Service** - Authentication, authorization, JWT token management, 2FA
- **User Service** - User profile management, KYC verification, beneficiary management
- **Account Service** - Account management, multiple account types
- **Transaction Service** - Money transfers, transaction history, statements
- **Notification Service** - Email/SMS notifications for transactions

## 🚀 Key Features

### Security Features
- ✅ **JWT-based Authentication** with refresh tokens
- ✅ **Two-Factor Authentication (2FA)** with TOTP
- ✅ **BCrypt Password Encryption** (12 rounds)
- ✅ **Account Lockout** after failed login attempts
- ✅ **Audit Logging** for all critical operations
- ✅ **Rate Limiting** at API Gateway level
- ✅ **Circuit Breaker Pattern** using Resilience4j
- ✅ **CORS Configuration** for frontend integration

### Banking Features
- ✅ User Registration & Login
- ✅ Account Management (Multiple account types: Savings, Current, Fixed Deposit)
- ✅ Money Transfer (Internal transfers)
- ✅ Transaction History with pagination
- ✅ Statement Generation (Date range)
- ✅ Beneficiary Management
- ✅ KYC Verification Workflow
- ✅ User Profile Management
- ✅ Email Notifications

### Technical Features
- ✅ Microservices Architecture
- ✅ Service Discovery with Eureka
- ✅ API Gateway with Spring Cloud Gateway
- ✅ Centralized Configuration
- ✅ Database per Service pattern
- ✅ RESTful APIs
- ✅ Docker Containerization
- ✅ Health Checks & Monitoring
- ✅ Feign Client for inter-service communication

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Redis 7.0+
- Docker & Docker Compose (optional, for containerized deployment)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Cloud**: Spring Cloud 2023.0.0
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Security**: Spring Security, JWT, BCrypt
- **Database**: MySQL 8.0
- **Cache**: Redis
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker
- **2FA**: Google Authenticator (TOTP)

## 📦 Installation & Setup

### Option 1: Running with Docker Compose (Recommended)

1. **Clone the repository**
```bash
git clone <repository-url>
cd backend-springboot
```

2. **Set up environment variables**
```bash
cp .env.example .env
# Edit .env file with your configuration
```

3. **Build and run with Docker Compose**
```bash
docker-compose up -d
```

4. **Verify services are running**
```bash
docker-compose ps
```

Services will be available at:
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- Auth Service: http://localhost:8081
- User Service: http://localhost:8082
- Account Service: http://localhost:8083
- Transaction Service: http://localhost:8084
- Notification Service: http://localhost:8085

### Option 2: Running Locally

1. **Start MySQL**
```bash
# Install and start MySQL server
mysql -u root -p
```

2. **Start Redis**
```bash
# Install and start Redis server
redis-server
```

3. **Start services in order**

```bash
# 1. Service Discovery
cd service-discovery
mvn spring-boot:run

# 2. Config Server (in a new terminal)
cd config-server
mvn spring-boot:run

# 3. API Gateway (in a new terminal)
cd api-gateway
mvn spring-boot:run

# 4. Auth Service (in a new terminal)
cd auth-service
mvn spring-boot:run

# 5. User Service (in a new terminal)
cd user-service
mvn spring-boot:run

# 6. Account Service (in a new terminal)
cd account-service
mvn spring-boot:run

# 7. Transaction Service (in a new terminal)
cd transaction-service
mvn spring-boot:run

# 8. Notification Service (in a new terminal)
cd notification-service
mvn spring-boot:run
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Setup 2FA
```http
POST http://localhost:8080/api/auth/2fa/setup
Authorization: Bearer <your-jwt-token>
```

### User Endpoints

#### Get User Profile
```http
GET http://localhost:8080/api/users/profile
Authorization: Bearer <your-jwt-token>
```

#### Update Profile
```http
PUT http://localhost:8080/api/users/profile
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "phone": "+1234567890",
  "address": "123 Main St",
  "city": "New York"
}
```

#### Submit KYC
```http
POST http://localhost:8080/api/users/kyc/submit
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "documentType": "PASSPORT",
  "documentNumber": "AB1234567",
  "documentUrl": "https://example.com/document.pdf"
}
```

### Transaction Endpoints

#### Transfer Money
```http
POST http://localhost:8080/api/transactions/send
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "senderAccountNumber": "1012345678",
  "receiverAccountNumber": "1087654321",
  "amount": 100.50,
  "remarks": "Payment for services"
}
```

#### Get Transaction History
```http
GET http://localhost:8080/api/transactions/history?accountNumber=1012345678&page=0&size=10
Authorization: Bearer <your-jwt-token>
```

#### Get Recent Transactions
```http
GET http://localhost:8080/api/transactions/recent?accountNumber=1012345678
Authorization: Bearer <your-jwt-token>
```

### Account Endpoints

#### Get User Accounts
```http
GET http://localhost:8080/api/accounts
Authorization: Bearer <your-jwt-token>
```

#### Get Account Details
```http
GET http://localhost:8080/api/accounts/{accountNumber}
Authorization: Bearer <your-jwt-token>
```

## 🔒 Security Implementation

### 1. Authentication Flow
- User registers/logs in → Receives JWT access token and refresh token
- JWT token is validated at API Gateway level
- User ID and email are extracted and passed to downstream services via headers

### 2. Two-Factor Authentication
- Users can enable 2FA from their profile
- QR code is generated for Google Authenticator
- Login requires both password and 2FA code when enabled

### 3. Password Security
- Passwords are hashed using BCrypt with 12 rounds
- Password strength validation
- Password expiry after 90 days

### 4. Account Protection
- Maximum 5 failed login attempts
- Account locked for 30 minutes after max attempts
- Audit logging of all authentication events

### 5. API Security
- Rate limiting (10 requests/second per user)
- CORS configured for specific origins
- Input validation on all endpoints
- SQL injection prevention via JPA

## 🏥 Health Monitoring

Each service exposes health endpoints:

```bash
# Check all services
curl http://localhost:8761  # Eureka Dashboard
curl http://localhost:8080/actuator/health  # API Gateway
curl http://localhost:8081/actuator/health  # Auth Service
curl http://localhost:8082/actuator/health  # User Service
curl http://localhost:8083/actuator/health  # Account Service
curl http://localhost:8084/actuator/health  # Transaction Service
curl http://localhost:8085/actuator/health  # Notification Service
```

## 🗄️ Database Schema

Each service has its own database:
- `dublefin_auth` - Users, refresh tokens, audit logs
- `dublefin_user` - User profiles, beneficiaries, KYC documents
- `dublefin_account` - Accounts, cards
- `dublefin_transaction` - Transactions, transfers

## 🧪 Testing

Run tests for each service:

```bash
cd <service-name>
mvn test
```

## 📊 Monitoring & Observability

### Eureka Dashboard
Access at http://localhost:8761 to view:
- Registered services
- Service health status
- Instance details

### Actuator Endpoints
Each service exposes:
- `/actuator/health` - Health status
- `/actuator/info` - Application info
- `/actuator/metrics` - Application metrics

## 🚀 Deployment

### Docker Deployment
```bash
# Build all services
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Kubernetes Deployment
Kubernetes manifests are available in the `k8s/` directory:

```bash
kubectl apply -f k8s/
```

## 🔧 Configuration

### Environment Variables
Key environment variables to configure:

- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret (min 64 characters)
- `EUREKA_PASSWORD` - Eureka server password
- `EMAIL_USERNAME` - SMTP email username
- `EMAIL_PASSWORD` - SMTP email password

### Application Properties
Configuration is centralized in Config Server under `config-server/src/main/resources/config/`

## 📝 Best Practices Implemented

1. **Microservices Design**
   - Single Responsibility Principle
   - Database per service
   - API Gateway pattern
   - Service Discovery pattern

2. **Security**
   - Defense in depth
   - Principle of least privilege
   - Encryption at rest and in transit
   - Security by design

3. **Code Quality**
   - Clean code principles
   - SOLID principles
   - DRY (Don't Repeat Yourself)
   - Proper exception handling

4. **DevOps**
   - Containerization
   - Health checks
   - Logging
   - Configuration management

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- DubleFin Development Team

## 🙏 Acknowledgments

- Spring Boot community
- Spring Cloud Netflix
- Netflix OSS
- All contributors to the open-source libraries used in this project

## 📞 Support

For support, email support@dublefin.com or create an issue in the repository.

---

**Note**: This is a development version. For production deployment, ensure:
- Change all default passwords and secrets
- Configure proper HTTPS/TLS
- Set up proper monitoring and logging (ELK stack, Prometheus, Grafana)
- Implement backup and disaster recovery
- Perform security audit and penetration testing
- Configure proper rate limiting and DDoS protection

