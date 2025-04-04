#!/bin/bash

# Development Environment Runner Script
echo "Starting Tradestar in Development Environment..."

# Set Spring Profile to dev
export SPRING_PROFILES_ACTIVE=dev

# Run the application using Maven
./mvnw spring-boot:run