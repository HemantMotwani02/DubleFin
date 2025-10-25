@echo off
REM DubleFin Banking - Service Startup Script for Windows
REM This script starts all microservices in the correct order

echo 🚀 Starting DubleFin Banking Microservices...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven 3.8 or higher.
    exit /b 1
)

REM Create logs directory if it doesn't exist
if not exist logs mkdir logs

echo 📦 Building all services...
echo.

REM Build all services
for %%s in (service-discovery config-server api-gateway auth-service user-service account-service transaction-service notification-service) do (
    echo Building %%s...
    cd %%s
    call mvn clean package -DskipTests
    cd ..
)

echo.
echo 🎯 Starting services in order...
echo.

REM 1. Start Service Discovery
echo 1. Starting Service Discovery (Eureka Server)...
cd service-discovery
start "Service Discovery" cmd /c "mvn spring-boot:run > ..\logs\service-discovery.log 2>&1"
cd ..
timeout /t 30

REM 2. Start Config Server
echo 2. Starting Config Server...
cd config-server
start "Config Server" cmd /c "mvn spring-boot:run > ..\logs\config-server.log 2>&1"
cd ..
timeout /t 20

REM 3. Start API Gateway
echo 3. Starting API Gateway...
cd api-gateway
start "API Gateway" cmd /c "mvn spring-boot:run > ..\logs\api-gateway.log 2>&1"
cd ..
timeout /t 20

REM 4. Start Auth Service
echo 4. Starting Auth Service...
cd auth-service
start "Auth Service" cmd /c "mvn spring-boot:run > ..\logs\auth-service.log 2>&1"
cd ..
timeout /t 20

REM 5. Start User Service
echo 5. Starting User Service...
cd user-service
start "User Service" cmd /c "mvn spring-boot:run > ..\logs\user-service.log 2>&1"
cd ..
timeout /t 15

REM 6. Start Account Service
echo 6. Starting Account Service...
cd account-service
start "Account Service" cmd /c "mvn spring-boot:run > ..\logs\account-service.log 2>&1"
cd ..
timeout /t 15

REM 7. Start Transaction Service
echo 7. Starting Transaction Service...
cd transaction-service
start "Transaction Service" cmd /c "mvn spring-boot:run > ..\logs\transaction-service.log 2>&1"
cd ..
timeout /t 15

REM 8. Start Notification Service
echo 8. Starting Notification Service...
cd notification-service
start "Notification Service" cmd /c "mvn spring-boot:run > ..\logs\notification-service.log 2>&1"
cd ..

echo.
echo ✅ All services started successfully!
echo.
echo 📊 Service URLs:
echo    - Eureka Dashboard: http://localhost:8761
echo    - API Gateway:      http://localhost:8080
echo    - Auth Service:     http://localhost:8081
echo    - User Service:     http://localhost:8082
echo    - Account Service:  http://localhost:8083
echo    - Transaction Service: http://localhost:8084
echo    - Notification Service: http://localhost:8085
echo.
echo 📝 Logs are available in the logs\ directory
echo.
echo To stop all services, close the command windows or run: taskkill /F /FI "WindowTitle eq Service*"
pause

