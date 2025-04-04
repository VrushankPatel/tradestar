#!/bin/bash

# Production Environment Runner Script
echo "Starting Tradestar in Production Environment..."

# Set Spring Profile to prod
export SPRING_PROFILES_ACTIVE=prod

# Set Database Configuration
export POSTGRES_URL="jdbc:postgresql://localhost:5432/tradestar"
export POSTGRES_USER="tradestar_user"
# Note: In actual production deployment, password should be securely managed
export POSTGRES_PASSWORD="your_secure_password"

# Set JWT Secret
export JWT_SECRET="your_production_jwt_secret"

# Run the application using Maven with production optimizations
./mvnw clean package -DskipTests
java -jar -Xms512m -Xmx1024m target/tradestar-*.jar