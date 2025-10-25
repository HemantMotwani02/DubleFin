#!/bin/bash

# DubleFin Banking - Service Startup Script
# This script starts all microservices in the correct order

echo "🚀 Starting DubleFin Banking Microservices..."
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=0
    
    echo -e "${BLUE}Waiting for $service_name to start on port $port...${NC}"
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
            echo -e "${GREEN}✓ $service_name is ready!${NC}"
            return 0
        fi
        attempt=$((attempt + 1))
        sleep 2
    done
    
    echo "⚠️  Warning: $service_name may not be ready yet"
    return 1
}

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.8 or higher."
    exit 1
fi

echo "📦 Building all services..."
echo ""

# Build all services
services=("service-discovery" "config-server" "api-gateway" "auth-service" "user-service" "account-service" "transaction-service" "notification-service")

for service in "${services[@]}"; do
    echo "Building $service..."
    cd "$service" && mvn clean package -DskipTests && cd .. || exit 1
done

echo ""
echo "🎯 Starting services in order..."
echo ""

# 1. Start Service Discovery
echo "1. Starting Service Discovery (Eureka Server)..."
cd service-discovery && mvn spring-boot:run > ../logs/service-discovery.log 2>&1 &
cd ..
wait_for_service "Service Discovery" 8761

# 2. Start Config Server
echo "2. Starting Config Server..."
cd config-server && mvn spring-boot:run > ../logs/config-server.log 2>&1 &
cd ..
wait_for_service "Config Server" 8888

# 3. Start API Gateway
echo "3. Starting API Gateway..."
cd api-gateway && mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
cd ..
wait_for_service "API Gateway" 8080

# 4. Start Auth Service
echo "4. Starting Auth Service..."
cd auth-service && mvn spring-boot:run > ../logs/auth-service.log 2>&1 &
cd ..
wait_for_service "Auth Service" 8081

# 5. Start User Service
echo "5. Starting User Service..."
cd user-service && mvn spring-boot:run > ../logs/user-service.log 2>&1 &
cd ..
wait_for_service "User Service" 8082

# 6. Start Account Service
echo "6. Starting Account Service..."
cd account-service && mvn spring-boot:run > ../logs/account-service.log 2>&1 &
cd ..
wait_for_service "Account Service" 8083

# 7. Start Transaction Service
echo "7. Starting Transaction Service..."
cd transaction-service && mvn spring-boot:run > ../logs/transaction-service.log 2>&1 &
cd ..
wait_for_service "Transaction Service" 8084

# 8. Start Notification Service
echo "8. Starting Notification Service..."
cd notification-service && mvn spring-boot:run > ../logs/notification-service.log 2>&1 &
cd ..
wait_for_service "Notification Service" 8085

echo ""
echo "✅ All services started successfully!"
echo ""
echo "📊 Service URLs:"
echo "   - Eureka Dashboard: http://localhost:8761"
echo "   - API Gateway:      http://localhost:8080"
echo "   - Auth Service:     http://localhost:8081"
echo "   - User Service:     http://localhost:8082"
echo "   - Account Service:  http://localhost:8083"
echo "   - Transaction Service: http://localhost:8084"
echo "   - Notification Service: http://localhost:8085"
echo ""
echo "📝 Logs are available in the logs/ directory"
echo ""
echo "To stop all services, run: ./stop-services.sh"

