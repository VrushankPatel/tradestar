# Tradestar - Advanced Trading Gateway Platform

![Java](https://img.shields.io/badge/Java-17-red.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-blue.svg)
![QuickFIX/J](https://img.shields.io/badge/QuickFIX/J-2.3.1-orange.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

Tradestar is an enterprise-grade trading gateway platform that provides robust market connectivity through industry-standard protocols. Built with Spring Boot, it offers a sophisticated environment for simulating and testing trading operations with high-performance market data handling.

## Core Features

### Protocol Support
- **FIX Protocol Integration**: 
  - Supports FIX 4.2 and 4.4 via QuickFIX/J
  - Order entry and execution reporting
  - Market data subscription
- **Market Data Handling**:
  - Real-time price updates
  - Order book management
  - Trade execution simulation

### Platform Capabilities
- **Real-time Processing**: WebSocket-based market data streaming
- **Security**: JWT-based authentication with comprehensive access control
- **Database Integration**: 
  - H2 for development environment
  - PostgreSQL for production deployment
- **API Documentation**: Auto-generated OpenAPI/Swagger documentation
- **Validation**: Extensive request validation and error handling

## Technical Architecture

### Tech Stack
- **Backend Framework**: Spring Boot 3.2.1
- **Java Version**: JDK 17
- **Database Systems**: 
  - H2 (Development)
  - PostgreSQL (Production)
- **Security**: JWT Authentication
- **Protocol Handler**: QuickFIX/J 2.3.1
- **API Documentation**: SpringDoc OpenAPI 2.3.0

### Core Components
- **Protocol Handlers**: Dedicated services for protocol message processing
- **Order Management**: Centralized order processing and routing
- **Market Data Service**: Real-time market data simulation
- **Security Layer**: Authentication and authorization management

## Getting Started

### Prerequisites
- Java 17 JDK
- Maven 3.8+
- PostgreSQL (for production deployment)

### Installation

1. Clone the repository
2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Start the application:
   - Development environment:
     ```bash
     ./run-dev.sh
     ```
   - Production environment:
     ```bash
     ./run-prod.sh
     ```

### API Documentation
Access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Vrushank Patel - vrushankpatel5@gmail.com

Project Link: [https://github.com/VrushankPatel/Tradestar](https://github.com/VrushankPatel/Tradestar)