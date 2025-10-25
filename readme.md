# DubleFin Banking System

> A modern, secure, and scalable online banking application migrated from Node.js to Java Spring Boot microservices architecture.

## 📋 Project Overview

DubleFin is an enterprise-grade online banking platform that demonstrates industry best practices in microservices architecture, security, and scalability. This project was migrated from Node.js to Java Spring Boot to leverage the robustness and security features of the Java ecosystem.

## ✨ Key Highlights

- **Microservices Architecture** - Scalable, maintainable, and fault-tolerant
- **Industry-Standard Security** - JWT, 2FA, BCrypt, Rate Limiting, Circuit Breakers
- **Modern UI** - React-based responsive frontend
- **Production-Ready** - Docker, Health Checks, Monitoring, Audit Logging
- **Best Practices** - SOLID principles, Clean Code, RESTful APIs

## 🏗️ Architecture

### Backend Microservices (Spring Boot)
- **Service Discovery** (Eureka Server) - Port 8761
- **Config Server** - Port 8888
- **API Gateway** - Port 8080
- **Auth Service** - Port 8081
- **User Service** - Port 8082
- **Account Service** - Port 8083
- **Transaction Service** - Port 8084
- **Notification Service** - Port 8085

### Frontend (React)
- Modern React 19 application
- Responsive design
- Material-UI components
- Port 3000

### Databases
- MySQL 8.0 - Relational database (separate DB per service)
- Redis 7.0 - Caching and rate limiting

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.8+
- Docker & Docker Compose (optional)

### Option 1: Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd DubleFin-main

# Start backend services
cd backend-springboot
docker-compose up -d

# Start frontend (in a new terminal)
cd frontend
npm install
npm start
```

### Option 2: Manual Setup

**Backend:**
```bash
cd backend-springboot

# Start services in order:
# 1. Service Discovery
cd service-discovery && mvn spring-boot:run

# 2. Config Server (new terminal)
cd config-server && mvn spring-boot:run

# 3. API Gateway (new terminal)
cd api-gateway && mvn spring-boot:run

# 4-8. Business Services (new terminals)
cd auth-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run
cd account-service && mvn spring-boot:run
cd transaction-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

## 📱 Features

### Authentication & Security
- ✅ User Registration & Login
- ✅ JWT-based Authentication
- ✅ Two-Factor Authentication (2FA/TOTP)
- ✅ Password Encryption (BCrypt)
- ✅ Account Lockout Protection
- ✅ Session Management
- ✅ Audit Logging

### Banking Operations
- ✅ Account Management
  - Multiple account types (Savings, Current, Fixed Deposit)
  - Account creation and management
  - Real-time balance updates
- ✅ Money Transfers
  - Internal transfers
  - Beneficiary management
  - Transaction validation
- ✅ Transaction History
  - Paginated history
  - Date range filtering
  - Statement generation
- ✅ KYC Verification
  - Document upload
  - Verification workflow
  - Status tracking

### User Management
- ✅ Profile Management
- ✅ Personal Information
- ✅ Contact Details
- ✅ Beneficiary Management
- ✅ Security Settings

### Notifications
- ✅ Email Notifications
- ✅ Transaction Alerts
- ✅ Security Alerts

## 🔒 Security Implementation

### Layer 1: API Gateway
- Rate limiting (10 req/sec per user)
- CORS configuration
- JWT validation
- Circuit breaker pattern

### Layer 2: Service Level
- Spring Security
- Input validation
- SQL injection prevention
- XSS protection

### Layer 3: Data
- Password hashing (BCrypt, 12 rounds)
- Sensitive data encryption
- Audit logging
- Database per service

### Layer 4: Infrastructure
- HTTPS/TLS
- Network segmentation
- Container security
- Health monitoring

## 📚 Documentation

- [Backend README](backend-springboot/README.md) - Detailed backend documentation
- [API Documentation](backend-springboot/README.md#-api-documentation) - API endpoints
- [Environment Configuration](backend-springboot/ENV_CONFIG.md) - Setup guide
- [Docker Guide](backend-springboot/docker-compose.yml) - Container orchestration

## 🛠️ Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Security
- MySQL 8.0
- Redis 7.0
- Maven
- Docker

### Frontend
- React 19
- React Router v7
- Axios
- Material-UI (optional)

### Infrastructure
- Netflix Eureka (Service Discovery)
- Spring Cloud Gateway (API Gateway)
- Spring Cloud Config (Configuration)
- Resilience4j (Circuit Breaker)
- Docker & Docker Compose

## 📈 Project Structure

```
DubleFin-main/
├── backend/                    # Legacy Node.js backend (for reference)
├── backend-springboot/         # New Spring Boot microservices
│   ├── api-gateway/
│   ├── service-discovery/
│   ├── config-server/
│   ├── auth-service/
│   ├── user-service/
│   ├── account-service/
│   ├── transaction-service/
│   ├── notification-service/
│   ├── docker-compose.yml
│   └── README.md
├── frontend/                   # React frontend
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── App.js
│   └── package.json
└── README.md                   # This file
```

## 🌐 Access Points

After starting all services:

- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Auth Service**: http://localhost:8081
- **User Service**: http://localhost:8082
- **Account Service**: http://localhost:8083
- **Transaction Service**: http://localhost:8084
- **Notification Service**: http://localhost:8085

## 🧪 Testing

```bash
# Backend tests
cd backend-springboot/<service-name>
mvn test

# Frontend tests
cd frontend
npm test
```

## 🔧 Configuration

### Environment Variables
Create a `.env` file in `backend-springboot/`:

```env
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=your-secret-key-min-64-chars
EUREKA_PASSWORD=eureka123
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Database Setup
MySQL databases will be created automatically:
- dublefin_auth
- dublefin_user
- dublefin_account
- dublefin_transaction

## 📊 Monitoring

### Health Checks
```bash
# Check all services
curl http://localhost:8761  # Eureka Dashboard

# Individual service health
curl http://localhost:8080/actuator/health  # API Gateway
curl http://localhost:8081/actuator/health  # Auth Service
# ... and so on
```

### Logs
```bash
# Docker logs
docker-compose logs -f <service-name>

# Application logs
tail -f <service-name>/logs/application.log
```

## 🚀 Deployment

### Docker
```bash
cd backend-springboot
docker-compose up -d
```

### Kubernetes
```bash
kubectl apply -f k8s/
```

## 📝 Migration Notes

### From Node.js to Spring Boot

**Improvements:**
1. **Type Safety** - Java's strong typing reduces runtime errors
2. **Security** - Spring Security provides robust authentication/authorization
3. **Scalability** - Better performance and resource management
4. **Maintainability** - Better structure and industry-standard patterns
5. **Enterprise Features** - Circuit breakers, distributed tracing, etc.

**Key Changes:**
- Express.js → Spring Boot Web
- Sequelize ORM → JPA/Hibernate
- JWT implementation → Spring Security JWT
- No equivalent → Service Discovery, API Gateway
- Basic auth → Enhanced security with 2FA, rate limiting

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- DubleFin Development Team

## 🙏 Acknowledgments

- Spring Boot and Spring Cloud communities
- React community
- All open-source contributors

## 📞 Support

For issues and questions:
- Create an issue in the repository
- Email: support@dublefin.com

---

**⚠️ Important Security Note:**

This is a demonstration project. For production use:
- Change all default passwords and secrets
- Enable HTTPS/TLS
- Implement proper monitoring
- Perform security audits
- Set up backup and disaster recovery
- Configure production-grade database and cache
- Implement proper logging and monitoring (ELK, Prometheus, Grafana)
- Use secrets management (AWS Secrets Manager, Azure Key Vault, etc.)

---

Made with ❤️ by the DubleFin Team
