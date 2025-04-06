# Keycloak Integration Setup Guide

## Overview
This document provides instructions for setting up and configuring Keycloak for the Tradestar application. Keycloak is used as the identity and access management solution, providing secure authentication and authorization.

## Prerequisites
- Docker (for running Keycloak in a container)
- Java 11 or higher
- Maven

## Running Keycloak

### Using Docker

```bash
docker run -p 8180:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```

This will start Keycloak on port 8180 with the admin username and password both set to "admin".

## Keycloak Configuration

### 1. Create a New Realm
1. Log in to the Keycloak Admin Console at http://localhost:8180/admin
2. Hover over the realm dropdown in the top-left corner and click "Create Realm"
3. Enter "tradestar" as the realm name and click "Create"

### 2. Create Client
1. In the left sidebar, click on "Clients"
2. Click "Create client"
3. Enter the following details:
   - Client ID: tradestar-app
   - Client Protocol: openid-connect
   - Root URL: http://localhost:8080
4. Click "Save"
5. On the next screen, configure:
   - Access Type: confidential
   - Valid Redirect URIs: http://localhost:8080/*
   - Web Origins: http://localhost:8080
6. Click "Save"
7. Go to the "Credentials" tab and note the Secret value - you'll need this for your application.properties

### 3. Create Roles
1. In the left sidebar, click on "Realm roles"
2. Click "Create role"
3. Create the following roles:
   - ADMIN
   - TRADER

### 4. Create Test Users
1. In the left sidebar, click on "Users"
2. Click "Add user"
3. Enter user details:
   - Username: admin@tradestar.com
   - Email: admin@tradestar.com
   - First Name: Admin
   - Last Name: User
   - Email Verified: ON
4. Click "Save"
5. Go to the "Credentials" tab
6. Set a password and disable "Temporary" option
7. Go to the "Role Mappings" tab
8. Add the "ADMIN" role to the user
9. Repeat steps 2-8 to create a trader user with the "TRADER" role

## Application Configuration

Update your `keycloak.properties` file with the correct values:

```properties
# Keycloak Server Configuration
keycloak.auth-server-url=http://localhost:8180/auth
keycloak.realm=tradestar
keycloak.resource=tradestar-app
keycloak.public-client=false
keycloak.credentials.secret=your-client-secret-from-keycloak
keycloak.bearer-only=false
keycloak.use-resource-role-mappings=true

# Keycloak Admin Configuration
keycloak.admin-username=admin
keycloak.admin-password=admin
```

## Testing the Integration

1. Start your Spring Boot application
2. Try to access a protected endpoint
3. You should be redirected to the Keycloak login page
4. After successful login, you should be redirected back to the application

## Troubleshooting

### Common Issues

1. **Connection refused**: Ensure Keycloak is running and accessible on the configured port
2. **Invalid redirect URI**: Check that the redirect URI in Keycloak matches your application's URL
3. **Invalid client credentials**: Verify the client secret in your properties file
4. **Role mapping issues**: Ensure roles are properly defined in both Keycloak and your application

### Logs

Enable debug logging for Keycloak and Spring Security to troubleshoot issues:

```properties
logging.level.org.keycloak=DEBUG
logging.level.org.springframework.security=DEBUG
```