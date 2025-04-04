# Tradestar - Trading Market Gateway Simulator API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

Tradestar is a sophisticated trading market gateway simulator API built with Spring Boot, designed to simulate financial market operations using the FIX protocol.

## Features

- **FIX Protocol Integration**: Supports FIX 4.2 and FIX 4.4 via QuickFIX/J
- **Security**: JWT-based authentication and Spring Security
- **WebSocket Support**: Real-time market data streaming
- **Database**: Supports both H2 (development) and PostgreSQL (production)
- **API Documentation**: Automatic OpenAPI/Swagger documentation
- **Validation**: Comprehensive request validation

## Tech Stack

- **Backend**: Spring Boot 3.2.1
- **Database**: H2 (dev), PostgreSQL (prod)
- **Authentication**: JWT
- **FIX Protocol**: QuickFIX/J 2.3.1
- **API Docs**: SpringDoc OpenAPI 2.3.0
- **Java**: JDK 17

## Getting Started

### Prerequisites

- Java 17 JDK
- Maven 3.8+
- PostgreSQL (for production)

### Installation

1. Clone the repository
2. Run `mvn clean install`
3. Start the application with `mvn spring-boot:run`

For development with H2 database:
```bash
./run-dev.sh
```

For production with PostgreSQL:
```bash
./run-prod.sh
```

## API Documentation

After starting the application, access the Swagger UI at:
`http://localhost:8080/swagger-ui.html`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Contact

Vrushank Patel - vrushankpatel5@gmail.com

Project Link: [https://github.com/VrushankPatel/Tradestar](https://github.com/VrushankPatel/Tradestar)