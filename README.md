# Dahua Face Scan API Client

A Spring Boot application that provides a REST API for interacting with Dahua's BRM (Biometric Recognition Management) system, specifically for face scanning and MQ configuration management.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Authentication Flow](#authentication-flow)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features

- Two-step authentication with Dahua BRM system
- Secure token-based session management
- MQ configuration retrieval
- RESTful API endpoints
- Comprehensive logging
- Swagger/OpenAPI documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Access to Dahua BRM system
- Valid credentials (username/password)

## Configuration

### Configuration Files

1. **Main Configuration** (`src/main/resources/application.properties`):
   - Contains default configuration values
   - Should be committed to version control with safe defaults
   - Example:
     ```properties
     # Application
     spring.application.name=face-scan
     
     # Server
     server.port=8080
     
     # WebSocket
     websocket.endpoint=/ws
     websocket.topic.prefix=/topic
     websocket.application.prefix=/app
     
     # Logging
     logging.level.root=INFO
     logging.level.com._i.dahua=DEBUG
     ```

2. **Environment-Specific Configuration** (`src/main/resources/application-{env}.properties`):
   - For environment-specific settings (e.g., `application-dev.properties`, `application-prod.properties`)
   - Should NOT contain sensitive data
   - Example for `application-dev.properties`:
     ```properties
     # Development environment settings
     spring.profiles.active=dev
     ```

3. **Local Override File** (`config/application-local.properties`):
   - Create a `config` directory in your project root
   - Add `application-local.properties` with your local settings
   - This file is in `.gitignore` to prevent committing sensitive data
   - Example:
     ```properties
     # Local development overrides
     dahua.api.base-url=https://180.180.217.182:443
     dahua.api.username=your_username
     dahua.api.password=your_password
     
     # MQTT Configuration (if needed)
     mqtt.broker.url=tcp://localhost:1883
     mqtt.client.id=local-dev-client
     mqtt.username=guest
     mqtt.password=guest
     mqtt.topic=test/topic
     ```

**Important Security Note**: Never commit sensitive information like passwords or API keys to version control. Use environment variables or the local override file for sensitive configurations.

```properties
# Application
spring.application.name=face.scan

# Server
server.port=8080

# Dahua API Configuration
dahua.api.base-url=https://180.180.217.182:443
dahua.api.username=system
dahua.api.password=your_password_here
dahua.api.client-type=WINPC_V2
dahua.api.ip-address=192.168.100.16

# Logging
logging.level.root=INFO
logging.level.com._i.dahua=DEBUG
logging.file.name=logs/face-scan.log
logging.file.max-size=10MB
logging.file.max-history=7
```

## API Endpoints

### 1. Authentication

#### Step 1: Initialize Authentication
```http
GET /api/auth/init
```
Initiates the authentication process and retrieves the authentication challenge.

#### Step 2: Complete Authentication
```http
POST /api/auth/complete
Content-Type: application/json

{
  "username": "system",
  "password": "your_password_here",
  "realm": "challenge_realm",
  "randomKey": "challenge_random_key"
}
```
Completes the authentication process and returns an access token.

### 2. MQ Configuration

#### Get MQ Configuration
```http
GET /api/mq/config
Authorization: Bearer {token}
```
Retrieves the MQ configuration from the Dahua BRM system.

## Authentication Flow

The application uses a two-step authentication process:

1. **Initial Authentication Request**:
   - Client sends a request to initialize authentication
   - Server responds with a challenge (realm and random key)

2. **Authentication Completion**:
   - Client generates a signature using the challenge and credentials
   - Client sends the signature to complete authentication
   - Server validates and returns an access token

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd face.scan
   ```

2. **Configure the application**
   - Update `application.properties` with your Dahua BRM credentials and settings

3. **Build the application**
   ```bash
   ./mvnw clean package
   ```

4. **Run the tests**
   ```bash
   # Run all tests
   ./mvnw test
   
   # Run a specific test class
   ./mvnw test -Dtest=DahuaApiClientTest
   
   # Run a specific test method
   ./mvnw test -Dtest=DahuaApiClientTest#testGetToken
   
   # Run with debug output
   ./mvnw test -Dmaven.surefire.debug
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the API**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

## Docker Deployment

### Prerequisites
- Docker installed on your system
- Access to the Dahua BRM system

### Building the Docker Image

1. **Build the Docker image**
   ```bash
   docker build -t 8i-inf-pmcu-poc .
   ```

2. **Run the container**
   ```bash
   docker run -p 8080:8080 \
     -e DAHUA_API_BASE_URL=https://180.180.217.182:443 \
     -e DAHUA_API_IP=192.168.100.16 \
     -e DAHUA_API_USER=system \
     -e DAHUA_API_PASSWORD=ismart123456 \
     8i-inf-pmcu-poc
   ```

### Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DAHUA_API_BASE_URL` | Base URL for Dahua API | `https://180.180.217.182:443` |
| `DAHUA_API_IP` | IP address for authentication | `192.168.100.16` |
| `DAHUA_API_USER` | Username for authentication | `system` |
| `DAHUA_API_PASSWORD` | Password for authentication | `your_password` |

### Accessing the Application
- The application will be available at: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/_i/dahua/face/scan/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── exception/       # Exception handling
│   │       ├── service/         # Business logic
│   │       └── Application.java # Main application class
│   └── resources/
│       ├── application.properties  # Application configuration
│       └── static/              # Static resources
└── test/                        # Test classes
```

## Troubleshooting

### Common Issues

1. **Authentication Failures**
   - Verify credentials in `application.properties`
   - Check network connectivity to Dahua BRM system
   - Ensure the Dahua service is running and accessible

2. **MQ Configuration Not Found**
   - Verify user permissions in Dahua BRM
   - Check if MQ service is properly configured in Dahua
   - Review server logs for detailed error messages

3. **MQ Connection Issues**
   - The application now includes robust MQ connection handling with automatic retries
   - Configure retry behavior using the following properties:
     ```properties
     # Number of times to retry connecting to MQ broker
     mq.connection.retry-attempts=3
     # Delay between retry attempts in milliseconds
     mq.connection.retry-delay=5000
     ```
   - Health checks periodically verify the connection and can automatically reconnect:
     ```properties
     # Interval between health checks in milliseconds (default: 5 minutes)
     mq.health-check.interval=300000
     # Enable/disable health checks
     mq.health-check.enabled=true
     # Automatically attempt to reconnect if connection is unhealthy
     mq.health-check.reconnect-on-failure=true
     ```
   - For authentication issues, you can provide fallback credentials via environment variables:
     ```bash
     # Override API-provided credentials with direct broker credentials
     export MQ_FALLBACK_USERNAME=your_broker_username
     export MQ_FALLBACK_PASSWORD=your_broker_password
     ```
   - The application now includes diagnostic endpoints to help troubleshoot MQ issues:
     ```
     # View raw MQ config from API
     GET /api/test/mq-config

     # Test MQ connection with custom credentials
     POST /api/test/mq-connection?username=test&password=test

     # Check MQ connection health
     GET /api/test/mq-health
     ```
   - See [MQ Authentication Troubleshooting](docs/MQ-AUTHENTICATION.md) for more details

3. **Log Files**
   - Application logs: `logs/face-scan.log`
   - Enable DEBUG logging for detailed troubleshooting

### Enabling Debug Logging

Add this to `application.properties` for detailed logging:

```properties
logging.level.com._i.dahua=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG
```

## License

This project is licensed under the [MIT License](LICENSE).
