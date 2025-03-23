# Test API Spring

A Spring Boot REST API example project demonstrating various features and best practices.

## Current Version: v0.7-swagger

### Features

- Basic Spring Boot REST API setup
- Health check endpoint at `/public/health`
- Authentication with JWT and Basic Auth:
  - JWT token generation at `/auth/login`
  - Secured endpoints with JWT at `/secure/**`
  - Secured endpoints with Basic Auth at `/basic-auth/**`
  - Public endpoints at `/public/**`
- User management CRUD operations:
  - Public endpoints at `/public/users/**`
  - Secured endpoints with JWT at `/secure/users/**`
  - Basic Auth secured endpoints at `/basic-auth/users/**`
- Product management CRUD operations:
  - Public endpoints at `/public/products/**`
  - Secured endpoints with JWT at `/secure/products/**`
  - Basic Auth secured endpoints at `/basic-auth/products/**`
- Global exception handling:
  - Standardized error responses
  - Custom exceptions for common error scenarios
  - Centralized error handling across all controllers
- Input validation:
  - Bean validation with Jakarta Validation
  - Group validation for different operations (create vs update)
  - Detailed validation error responses
- API Documentation with Swagger UI:
  - Interactive API documentation at `/swagger-ui.html`
  - OpenAPI specification at `/api-docs`
  - Detailed schema information for all models
  - Authentication support in Swagger UI
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

### Using Swagger UI

After starting the application, you can access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

The Swagger UI provides:

- A complete listing of all API endpoints grouped by controller
- Interactive testing of API endpoints directly from the browser
- Detailed model schemas and request/response examples
- Support for authentication via JWT tokens and Basic Auth
- Ability to download the OpenAPI specification

For raw OpenAPI specification in JSON format, visit:

```
http://localhost:8080/api-docs
```

### Testing

```bash
mvn test
```

### Available Endpoints

- **Public Endpoints (No Authentication Required)**:

  - GET `/public/health` - Health check endpoint
  - GET `/public/message` - Public message endpoint
  - POST `/auth/login` - Authentication endpoint to get JWT token
  - GET `/public/users` - Get all users
  - GET `/public/users/{id}` - Get user by ID
  - GET `/public/users/username/{username}` - Get user by username
  - POST `/public/users` - Create new user
  - PUT `/public/users/{id}` - Update user
  - DELETE `/public/users/{id}` - Delete user

- **JWT-Secured Endpoints (Requires JWT Token)**:

  - GET `/secure/message` - Secured message endpoint with JWT
  - GET `/secure/users` - Get all users (JWT secured)
  - GET `/secure/users/{id}` - Get user by ID (JWT secured)
  - GET `/secure/users/username/{username}` - Get user by username (JWT secured)
  - POST `/secure/users` - Create new user (JWT secured)
  - PUT `/secure/users/{id}` - Update user (JWT secured)
  - DELETE `/secure/users/{id}` - Delete user (JWT secured)

- **Basic Auth-Secured Endpoints (Requires Username/Password)**:
  - GET `/basic-auth/message` - Secured message endpoint with Basic Auth
  - GET `/basic-auth/users` - Get all users (Basic Auth secured)
  - GET `/basic-auth/users/{id}` - Get user by ID (Basic Auth secured)
  - GET `/basic-auth/users/username/{username}` - Get user by username (Basic Auth secured)
  - POST `/basic-auth/users` - Create new user (Basic Auth secured)
  - PUT `/basic-auth/users/{id}` - Update user (Basic Auth secured)
  - DELETE `/basic-auth/users/{id}` - Delete user (Basic Auth secured)

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

### User Model

```json
{
	"id": 1,
	"username": "john_doe",
	"password": "password123", // Only required when creating/updating, not returned in responses
	"email": "john.doe@example.com",
	"role": "ROLE_USER"
}
```

### Changelog

#### v0.7-swagger

- Added SpringDoc OpenAPI for API documentation
- Configured Swagger UI with JWT and Basic Auth security schemes
- Added detailed API documentation annotations to all controllers
- Added schema documentation to all model classes
- Configured security to allow access to Swagger UI endpoints

#### v0.6-validation

- Added Jakarta Validation annotations to models
- Implemented input validation for all create and update operations
- Created validation groups for different operations (Create vs Update)
- Enhanced error handling for validation errors
- Made password required only for create operations

#### v0.5-errors

- Added global exception handling
- Created custom exceptions for common error scenarios
- Standardized error responses across the API
- Updated services to throw appropriate exceptions
- Simplified controller implementations

#### v0.4-crud-products

- Added Product model
- Implemented ProductRepository for data access
- Added ProductService for business logic
- Created public, JWT-secured, and Basic Auth-secured Product CRUD endpoints
- Added unit tests for all endpoints

#### v0.3-crud-users

- Added User model
- Implemented UserRepository for data access
- Added UserService for business logic
- Created public, JWT-secured, and Basic Auth-secured User CRUD endpoints
- Added unit tests for all endpoints

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

1. v0.6-validation: Input data validation
2. v0.7-swagger: Swagger UI integration
3. v0.8-cleanup: Code cleanup and documentation update
