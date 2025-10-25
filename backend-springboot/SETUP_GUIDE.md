# DubleFin Banking - Complete Setup Guide

This guide will walk you through setting up the DubleFin Banking application from scratch.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Database Setup](#database-setup)
3. [Redis Setup](#redis-setup)
4. [Environment Configuration](#environment-configuration)
5. [Building the Application](#building-the-application)
6. [Running with Docker](#running-with-docker)
7. [Running Locally](#running-locally)
8. [Frontend Setup](#frontend-setup)
9. [Verification](#verification)
10. [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/
   - Verify installation: `java -version`

2. **Apache Maven 3.8+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **MySQL 8.0+**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Verify installation: `mysql --version`

4. **Redis 7.0+**
   - Windows: https://github.com/microsoftarchive/redis/releases
   - Mac: `brew install redis`
   - Linux: `sudo apt-get install redis-server`
   - Verify installation: `redis-cli ping` (should return PONG)

5. **Node.js 18+ and npm**
   - Download from: https://nodejs.org/
   - Verify installation: `node -version` and `npm -version`

6. **Docker and Docker Compose** (Optional, for containerized deployment)
   - Download from: https://www.docker.com/products/docker-desktop
   - Verify installation: `docker -version` and `docker-compose -version`

## Database Setup

### Windows

1. **Install MySQL**
   - Run the MySQL installer
   - Set root password during installation

2. **Start MySQL Service**
   ```powershell
   # Start MySQL service
   net start MySQL80
   ```

3. **Verify Connection**
   ```powershell
   mysql -u root -p
   # Enter password when prompted
   ```

### Linux/Mac

1. **Install MySQL**
   ```bash
   # Mac
   brew install mysql
   
   # Ubuntu/Debian
   sudo apt-get update
   sudo apt-get install mysql-server
   ```

2. **Start MySQL**
   ```bash
   # Mac
   brew services start mysql
   
   # Linux
   sudo systemctl start mysql
   ```

3. **Secure Installation**
   ```bash
   sudo mysql_secure_installation
   ```

### Database Configuration

The application will automatically create required databases on startup:
- `dublefin_auth`
- `dublefin_user`
- `dublefin_account`
- `dublefin_transaction`

**Note:** Make sure MySQL is running and accessible on `localhost:3306`

## Redis Setup

### Windows

1. Download Redis from https://github.com/microsoftarchive/redis/releases
2. Extract and run `redis-server.exe`

### Mac

```bash
brew install redis
brew services start redis
```

### Linux

```bash
sudo apt-get install redis-server
sudo systemctl start redis
```

### Verify Redis

```bash
redis-cli ping
# Should return: PONG
```

## Environment Configuration

### Create Environment File

1. Navigate to `backend-springboot` directory
2. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

3. Edit `.env` file with your configuration:
   ```env
   # Database
   DB_USERNAME=root
   DB_PASSWORD=your_mysql_password

   # JWT Secret (generate a strong random string)
   JWT_SECRET=yourSecretKeyForJWTWhichShouldBeVeryLongAndSecureForProductionUse

   # Service Discovery
   EUREKA_PASSWORD=eureka123

   # Email (optional, for notifications)
   EMAIL_USERNAME=your-email@gmail.com
   EMAIL_PASSWORD=your-app-password
   ```

### Gmail Setup for Notifications (Optional)

1. Enable 2-Step Verification in your Google Account
2. Generate an App Password:
   - Go to: https://myaccount.google.com/apppasswords
   - Select "Mail" and your device
   - Copy the generated password
3. Use this password as `EMAIL_PASSWORD` in `.env`

## Building the Application

### Option 1: Build All Services at Once

```bash
cd backend-springboot

# Windows
build-all.bat

# Linux/Mac
./build-all.sh
```

### Option 2: Build Services Individually

```bash
cd backend-springboot

# Build each service
cd service-discovery && mvn clean package -DskipTests && cd ..
cd config-server && mvn clean package -DskipTests && cd ..
cd api-gateway && mvn clean package -DskipTests && cd ..
cd auth-service && mvn clean package -DskipTests && cd ..
cd user-service && mvn clean package -DskipTests && cd ..
cd account-service && mvn clean package -DskipTests && cd ..
cd transaction-service && mvn clean package -DskipTests && cd ..
cd notification-service && mvn clean package -DskipTests && cd ..
```

## Running with Docker

### Prerequisites
- Docker and Docker Compose installed
- MySQL and Redis containers will be started automatically

### Start All Services

```bash
cd backend-springboot

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps
```

### Stop All Services

```bash
docker-compose down
```

## Running Locally

### Prerequisites
- MySQL running on localhost:3306
- Redis running on localhost:6379

### Start Services Manually

#### Windows

```powershell
cd backend-springboot

# Run the startup script
start-services.bat
```

#### Linux/Mac

```bash
cd backend-springboot

# Make the script executable
chmod +x start-services.sh

# Run the startup script
./start-services.sh
```

### Start Services in Separate Terminals

**Terminal 1: Service Discovery**
```bash
cd service-discovery
mvn spring-boot:run
```

**Terminal 2: Config Server** (wait 30 seconds after Service Discovery)
```bash
cd config-server
mvn spring-boot:run
```

**Terminal 3: API Gateway** (wait 20 seconds after Config Server)
```bash
cd api-gateway
mvn spring-boot:run
```

**Terminal 4: Auth Service**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 5: User Service**
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 6: Account Service**
```bash
cd account-service
mvn spring-boot:run
```

**Terminal 7: Transaction Service**
```bash
cd transaction-service
mvn spring-boot:run
```

**Terminal 8: Notification Service**
```bash
cd notification-service
mvn spring-boot:run
```

## Frontend Setup

### Install Dependencies

```bash
cd frontend
npm install
```

### Configure Backend URL

The frontend is already configured to connect to `http://localhost:8080` (API Gateway).

### Start Frontend

```bash
npm start
```

The application will open at http://localhost:3000

## Verification

### Check Service Health

1. **Eureka Dashboard**: http://localhost:8761
   - All services should be registered and UP

2. **Individual Services**:
   ```bash
   curl http://localhost:8080/actuator/health  # API Gateway
   curl http://localhost:8081/actuator/health  # Auth Service
   curl http://localhost:8082/actuator/health  # User Service
   curl http://localhost:8083/actuator/health  # Account Service
   curl http://localhost:8084/actuator/health  # Transaction Service
   curl http://localhost:8085/actuator/health  # Notification Service
   ```

### Test Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Test Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## Troubleshooting

### Common Issues

#### 1. Port Already in Use

**Error:** "Port 8080 is already in use"

**Solution:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080
kill -9 <PID>
```

#### 2. MySQL Connection Refused

**Error:** "Communications link failure"

**Solution:**
- Verify MySQL is running: `mysql -u root -p`
- Check MySQL port: `netstat -an | grep 3306`
- Verify credentials in `.env` file
- Check firewall settings

#### 3. Redis Connection Error

**Error:** "Unable to connect to Redis"

**Solution:**
- Verify Redis is running: `redis-cli ping`
- Start Redis service
- Check Redis port: `netstat -an | grep 6379`

#### 4. Service Not Registering with Eureka

**Solution:**
- Wait 30 seconds for registration
- Check Eureka dashboard: http://localhost:8761
- Verify service logs
- Restart the service

#### 5. Build Failures

**Error:** "Failed to execute goal"

**Solution:**
```bash
# Clear Maven cache
mvn dependency:purge-local-repository

# Rebuild
mvn clean install -U
```

#### 6. Frontend Cannot Connect to Backend

**Solution:**
- Verify API Gateway is running: http://localhost:8080
- Check browser console for CORS errors
- Verify API endpoints in frontend code
- Clear browser cache

### Log Files

Logs are available in:
- **Docker**: `docker-compose logs -f <service-name>`
- **Local**: `backend-springboot/logs/<service-name>.log`

### Getting Help

If you encounter issues:
1. Check the logs for error messages
2. Verify all prerequisites are installed
3. Ensure all required ports are available
4. Check environment variables
5. Restart the problematic service
6. Create an issue in the repository with:
   - Error message
   - Steps to reproduce
   - Environment details (OS, Java version, etc.)

## Next Steps

After successful setup:

1. **Explore the Application**
   - Register a new user
   - Complete KYC verification
   - Add beneficiaries
   - Make transactions

2. **Enable 2FA**
   - Go to Security Settings
   - Scan QR code with Google Authenticator
   - Test 2FA login

3. **Monitor Services**
   - Check Eureka dashboard
   - Review health endpoints
   - Monitor logs

4. **Production Preparation**
   - Change all default passwords
   - Set up proper secrets management
   - Configure HTTPS
   - Set up monitoring and alerting
   - Implement backup strategy

## Resources

- [Main README](README.md)
- [API Documentation](README.md#-api-documentation)
- [Environment Configuration](ENV_CONFIG.md)
- [Docker Guide](docker-compose.yml)

---

**Need Help?** Create an issue or contact support@dublefin.com

