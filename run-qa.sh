#!/bin/bash

# QA Environment Runner Script
echo "Starting Tradestar in QA Environment..."

# Set Spring Profile to qa
export SPRING_PROFILES_ACTIVE=qa

# Run the application using Maven
./mvnw clean verify spring-boot:run