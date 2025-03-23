# Test API Spring

A Spring Boot REST API example project demonstrating various features and best practices.

## Current Version: v0.2-auth

### Features

- Basic Spring Boot REST API setup
- Health check endpoint at `/public/health`
- Authentication with JWT and Basic Auth:
  - JWT token generation at `/auth/login`
  - Secured endpoints with JWT at `/secure/**`
  - Secured endpoints with Basic Auth at `/basic-auth/**`
  - Public endpoints at `/public/**`
- Maven build configuration
- Unit tests for controllers and security

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

- **Public Endpoints (No Authentication Required)**:

  - GET `/public/health` - Health check endpoint
  - GET `/public/message` - Public message endpoint
  - POST `/auth/login` - Authentication endpoint to get JWT token

- **JWT-Secured Endpoints (Requires JWT Token)**:

  - GET `/secure/message` - Secured message endpoint with JWT

- **Basic Auth-Secured Endpoints (Requires Username/Password)**:
  - GET `/basic-auth/message` - Secured message endpoint with Basic Auth

### Authentication

#### JWT Authentication

1. Get a JWT token by sending a POST request to `/auth/login` with the following JSON body:

```json
{
	"username": "admin",
	"password": "admin"
}
```

2. Use the returned token in the `Authorization` header for secured endpoints:

```
Authorization: Bearer your_jwt_token_here
```

#### Basic Authentication

Use HTTP Basic Authentication header with the following credentials:

- Username: admin
- Password: admin

Or:

- Username: user
- Password: user

### Changelog

#### v0.2-auth

- Added Spring Security configuration
- Implemented JWT authentication
- Added Basic Auth support
- Created secured endpoints
- Added unit tests for authentication

#### v0.1-init

- Basic Spring Boot REST API setup
- Health check endpoint at `/public/health`
- Maven build configuration
- Initial test coverage

### Upcoming Features (Planned Milestones)

1. v0.3-crud-users: User management CRUD operations
2. v0.4-crud-products: Product management CRUD operations
3. v0.5-exception-handling: Global exception handling
4. v0.6-validation: Input data validation
5. v0.7-swagger: Swagger UI integration
6. v0.8-cleanup: Code cleanup and documentation update
