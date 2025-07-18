# 🚀 FX Deals Warehouse API Documentation

FX Deals Warehouse exposes a powerful API for handling Foreign Exchange (FX) deals with comprehensive validation, error handling, and robust data persistence. This documentation provides complete details about the API endpoint, validation, error handling, database interactions, and deployment using Docker.

## 📋 Table of Contents
- [API Endpoint](#api-endpoint)
- [Request Validation](#request-validation)
- [Response Format](#response-format)
- [Database Interaction](#database-interaction)
- [Error Handling](#error-handling)
- [Architecture Features](#architecture-features)
- [Testing](#testing)
- [Dockerization](#dockerization)
- [Running the Application](#running-the-application)
- [API Usage Examples](#api-usage-examples)
- [Troubleshooting](#troubleshooting)

## 🎯 API Endpoint

### Primary Endpoint:
- **[POST] http://localhost:8080/api/FXdeals**


### Request JSON Example:
```json
{
    "id": "FX_DEAL_001",
    "orderingCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealAmount": 1500.75
}
```

## ✅ Request Validation

The endpoint performs comprehensive field validation using **Jakarta Validation** annotations:

### Validation Rules:
- **id**: Required, non-empty, 3-255 characters
- **orderingCurrencyIsoCode**: Required, non-empty, 3-255 characters
- **toCurrencyIsoCode**: Required, non-empty, 3-255 characters
- **dealAmount**: Required, must be greater than 0

### Validation Implementation:
- Uses `@NotNull`, `@NotEmpty`, `@Size`, and `@Min` annotations
- Invalid fields trigger structured error messages
- Handled by `GlobalExceptionHandler` REST Controller Advice
- Returns HTTP status `400 BAD_REQUEST` with detailed field errors

## 📤 Response Format

### Successful Creation (HTTP 201):
```json
{
    "id": "FX_DEAL_001",
    "orderingCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2025-07-18T10:30:45.123456",
    "dealAmount": 1500.75
}
```

### Validation Error (HTTP 400):
```json
{
    "dealAmount": "Deal amount is required",
    "toCurrencyIsoCode": "To currency iso code must not be null"
}
```

### Duplicate Deal Error (HTTP 409):
```json
{
    "error": "Request is already imported."
}
```

## 🗄️ Database Interaction

### Database Configuration:
- **Database**: PostgreSQL 15
- **Table**: `foreign_exchange_deals`
- **Connection Pool**: HikariCP
- **ORM**: Hibernate/JPA

### Business Logic:
- **New Deal**: If the FX deal with the specified `id` does not exist:
    - The object is validated and inserted into the database
    - A `dealTimestamp` is automatically generated using `@CreationTimestamp`
    - The complete inserted object is returned

- **Duplicate Deal**: If the FX deal with the specified `id` already exists:
    - A `RequestAlreadyExistException` is thrown
    - The exception is caught by `GlobalExceptionHandler`
    - Returns structured error message with HTTP status `409 CONFLICT`

### Database Schema:
```sql
CREATE TABLE foreign_exchange_deals (
    id VARCHAR(255) PRIMARY KEY,
    ordering_currency_iso_code VARCHAR(255) NOT NULL,
    to_currency_iso_code VARCHAR(255) NOT NULL,
    deal_timestamp TIMESTAMP(6),
    deal_amount DOUBLE PRECISION NOT NULL
);
```

## 🛡️ Error Handling

### Global Exception Handler:
- **Class**: `GlobalExceptionHandler` with `@RestControllerAdvice`
- **Validation Errors**: Handles `MethodArgumentNotValidException`
- **Business Logic Errors**: Handles custom exceptions like `RequestAlreadyExistException`
- **Generic Errors**: Provides fallback handling for unexpected exceptions

### Error Response Structure:
- Consistent JSON error format
- Appropriate HTTP status codes
- Field-level validation messages
- User-friendly error descriptions

## 🏗️ Architecture Features

### Aspect-Oriented Programming (AOP) Logging:
- **Framework**: AspectJ
- **Purpose**: Captures parameters passed to service methods
- **Coverage**: Logs successful executions and errors
- **Benefits**: Comprehensive insights without cluttering business logic

### Design Patterns:
- **DTO Pattern**: Separation between API layer (`FXDealDto`) and entity layer (`FXDeal`)
- **Repository Pattern**: Data access abstraction with Spring Data JPA
- **Service Layer**: Business logic encapsulation
- **Global Exception Handling**: Centralized error management

### Configuration Management:
- **Environment-based Configuration**: Uses environment variables for Docker deployment
- **Default Values**: Fallback configuration for local development
- **Security**: Database credentials externalized

## 🧪 Testing

### Unit Testing Framework:
- **JUnit 5**: Modern testing framework with comprehensive assertions
- **Mockito**: Efficient mocking for isolated unit tests
- **Test Coverage**: Comprehensive test suite ensuring robustness
- **Test Categories**:
    - Controller layer tests
    - Service layer tests
    - Repository layer tests
    - Integration tests

### Testing Best Practices:
- Mock external dependencies
- Test both positive and negative scenarios
- Validate error handling paths
- Ensure proper validation behavior

## 🐳 Dockerization

### Multi-Stage Dockerfile:
- **Build Stage**: Uses Maven 
- **Optimization**: Reduces final image size and improves security
- **Health Checks**: Built-in application health monitoring

### Docker Compose Architecture:
Docker Compose orchestrates deployment of two services:

#### Service 1: Spring Boot Application
- **Container Name**: `fx-deals-warehouse-api`
- **Port**: 8080 (mapped to host 8080)
- **Environment**: Production-ready configuration
- **Dependencies**: Waits for PostgreSQL to be healthy

#### Service 2: PostgreSQL Database
- **Container Name**: `fx-deals-postgres-db`
- **Image**: postgres:15-alpine
- **Port**: 5432 (mapped to host 5432)
- **Data Persistence**: Named volume for data retention
- **Health Checks**: Ensures database readiness before app startup

### Network Configuration:
- **Custom Bridge Network**: `fx-deals-network`
- **Service Discovery**: Containers communicate using service names
- **Isolation**: Dedicated network for security

## 🚀 Running the Application

### Prerequisites:
- Docker Desktop installed and running
- Git (for cloning the repository)
- 8080 and 5432 ports available

### Quick Start:
```bash
# Clone the repository
git clone https://github.com/Elmehdi-Erraji/fx-deals-warehouse
cd fx-deals-warehouse

# Start the application stack
docker-compose up --build

# Run in detached mode (background)
docker-compose up -d --build
```

### Alternative Commands:
```bash
# Build without cache
docker-compose build --no-cache

# View logs
docker-compose logs -f fx-deals-app
docker-compose logs -f fx-postgres

# Stop the application
docker-compose down

# Stop and remove volumes (⚠️ deletes data)
docker-compose down -v
```

### Access Points:
- **API Endpoint**: http://localhost:8080/api/FXdeals
- **Database**: localhost:5432 (postgres://fx_user:fx_pass123@localhost:5432/fx_deals_warehouse)

## 📚 API Usage Examples

### Using cURL:

#### Create a New FX Deal:
```bash
curl -X POST http://localhost:8080/api/FXdeals \
  -H "Content-Type: application/json" \
  -d '{
    "id": "CURL_TEST_001",
    "orderingCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealAmount": 2500.50
  }'
```

#### Test Validation (Missing Field):
```bash
curl -X POST http://localhost:8080/api/FXdeals \
  -H "Content-Type: application/json" \
  -d '{
    "id": "INVALID_TEST",
    "orderingCurrencyIsoCode": "USD"
  }'
```

#### Test Duplicate Prevention:
```bash
# First request (should succeed)
curl -X POST http://localhost:8080/api/FXdeals \
  -H "Content-Type: application/json" \
  -d '{
    "id": "DUPLICATE_TEST",
    "orderingCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealAmount": 1000.00
  }'

# Second request with same ID (should fail)
curl -X POST http://localhost:8080/api/FXdeals \
  -H "Content-Type: application/json" \
  -d '{
    "id": "DUPLICATE_TEST",
    "orderingCurrencyIsoCode": "GBP",
    "toCurrencyIsoCode": "JPY",
    "dealAmount": 1500.00
  }'
```

### Using PowerShell:
```powershell
# Test API with PowerShell
$body = @{
    id = "PS_TEST_001"
    orderingCurrencyIsoCode = "GBP"
    toCurrencyIsoCode = "JPY"
    dealAmount = 3000.25
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/FXdeals" -Method Post -Body $body -ContentType "application/json"
```

## 🔧 Troubleshooting

### Common Issues and Solutions:

#### Port Already in Use:
```bash
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill the process (Windows)
taskkill /PID <PID> /F

# Or use different port in docker-compose.yml
ports:
  - "8081:8080"  # Use 8081 instead
```

#### Database Connection Issues:
```bash
# Check PostgreSQL container health
docker-compose logs fx-postgres

# Test database connection
docker exec -it fx-deals-postgres-db psql -U fx_user -d fx_deals_warehouse
```

#### Container Restart Loops:
```bash
# Check application logs
docker-compose logs fx-deals-app

# Common causes:
# - Database not ready
# - Configuration errors
# - Memory issues
```

#### Network Issues:
```bash
# Recreate Docker network
docker-compose down
docker network prune
docker-compose up --build
```


### Development Mode:
For local development without Docker:
```bash
# Start only PostgreSQL in Docker
docker-compose up fx-postgres

# Run application locally
mvn spring-boot:run
```

## 📝 Configuration

### Environment Variables:
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SERVER_ADDRESS`: Server bind address (default: 0.0.0.0)

### Profiles:
- **Default**: Local development configuration
- **Docker**: Container-optimized configuration with environment variable overrides

---

## 📞 Support

For issues, questions, or contributions, please feel free to contact me.
- **Email**: elmehdi-erraji@hotmail.com

**API Version**: 1.0  
**Last Updated**: 2025-07-18  
**Compatibility**: Java 17+, Spring Boot 3.x, PostgreSQL 15+

