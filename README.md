# Test API Spring

A Spring Boot REST API example project demonstrating various features and best practices.

## Current Version: v0.1-init

### Features

- Basic Spring Boot REST API setup
- Health check endpoint at `/public/health`
- Maven build configuration
- Initial test coverage

### Requirements

- Java 17 or higher
- Maven 3.6 or higher

### Building the Project

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080 by default.

### Testing

```bash
mvn test
```

### Available Endpoints

- GET `/public/health` - Health check endpoint (public access)

### Upcoming Features (Planned Milestones)

1. v0.2-auth: JWT and Basic Auth implementation
2. v0.3-crud-users: User management CRUD operations
3. v0.4-crud-products: Product management CRUD operations
4. v0.5-exception-handling: Global exception handling
5. v0.6-validation: Input data validation
6. v0.7-swagger: Swagger UI integration
7. v0.8-cleanup: Code cleanup and documentation update
