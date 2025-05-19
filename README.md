# Spring Boot Starter Kit

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/com.bensamir/spring-boot-starter-kit.svg)](https://search.maven.org/artifact/com.bensamir/spring-boot-starter-kit)
[![Java 17+](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5+-green.svg)](https://spring.io/projects/spring-boot)

A comprehensive, production-ready Spring Boot starter kit with enterprise-grade features, designed to accelerate application development while enforcing best practices.

## 📋 Table of Contents

- [Features](#-features)
- [Getting Started](#-getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
- [Usage](#-usage)
   - [Basic Usage](#basic-usage)
   - [Component Overview](#component-overview)
- [Configuration](#-configuration)
   - [Common Properties](#common-properties)
   - [Exception Handling](#exception-handling)
   - [API Documentation](#api-documentation)
   - [Security/JWT](#securityjwt)
   - [Logging](#logging)
   - [Web Configuration](#web-configuration)
   - [Persistence](#persistence)
- [API Response Format](#-api-response-format)
- [Exception Handling](#-exception-handling)
- [Base Entities](#-base-entities)
- [Security & JWT](#-security--jwt)
- [Logging](#-logging)
- [API Documentation](#-api-documentation)
- [Examples](#-examples)
- [Contributing](#-contributing)
- [License](#-license)

## 🔥 Features

- **Unified API Response Format**: Consistent response structure with support for data, metadata, error handling, and pagination
- **Comprehensive Exception Handling**: Standardized error responses, internationalization support, and detailed validation errors
- **Base Entities & Persistence Utilities**: Audited entities with created/modified timestamps, optimistic locking, and UUID support
- **Security & JWT Authentication**: Ready-to-use JWT authentication with token generation, validation, and Spring Security configuration
- **API Documentation**: Integrated Swagger/OpenAPI documentation with customizable information and grouping
- **Enterprise Logging**: Request/response logging, MDC context for distributed tracing, and performance monitoring
- **Web Configuration**: CORS support, response compression, character encoding, and other essentials
- **Highly Configurable**: Fine-grained control via properties with sensible defaults

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven or Gradle
- Spring Boot 3.4.x or higher

### Installation

#### Maven

```xml
<dependency>
    <groupId>com.bensamir</groupId>
    <artifactId>spring-boot-starter-kit</artifactId>
    <version>0.2.0</version>
</dependency>
```

#### Gradle

```groovy
implementation 'com.bensamir:spring-boot-starter-kit:0.2.0'
```

## 🔍 Usage

### Basic Usage

Just add the dependency to your project, and the starter kit will automatically configure all components.

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### Component Overview

The starter kit includes several auto-configured components:

1. **`StarterKitAutoConfiguration`**: Main entry point for auto-configuration
2. **`ExceptionHandlingAutoConfiguration`**: Sets up global exception handling and i18n support
3. **`SecurityAutoConfiguration`**: Configures JWT security components
4. **`PersistenceAutoConfiguration`**: Sets up auditing and base entity support
5. **`WebConfigAutoConfiguration`**: Configures CORS, compression, and other web settings
6. **`ApiDocsAutoConfiguration`**: Sets up OpenAPI/Swagger documentation
7. **`ResponseAutoConfiguration`**: Configures JSON serialization for API responses
8. **`LoggingAutoConfiguration`**: Sets up request logging, MDC context, and other logging features

## ⚙️ Configuration

The starter kit provides extensive configuration options via properties. All properties have sensible defaults and are prefixed with `starter-kit`.

### Common Properties

The following properties can be configured in your `application.yml` or `application.properties` file:

```yaml
starter-kit:
  # Component-specific configurations - see below
```

### Exception Handling

```yaml
starter-kit:
  exception-handling:
    enabled: true                 # Enable/disable exception handling (default: true)
    log-exceptions: true          # Log exceptions (default: true)
    include-stack-trace: false    # Include stack traces in responses (default: false)
    enable-i18n: false            # Enable internationalization of error messages (default: false)
    default-messages:             # Default error messages
      resource.notfound: "Resource not found"
      # ... other message keys
```

### API Documentation

```yaml
starter-kit:
  api-docs:
    enabled: true                 # Enable/disable API docs (default: true)
    title: "API Documentation"    # API title (default: "API Documentation")
    description: "API Documentation generated by Spring Boot Starter Kit"
    version: "1.0"                # API version (default: "1.0")
    contact:
      name: "Contact Name"        # Contact name
      email: "contact@example.com" # Contact email
      url: "https://example.com"  # Contact URL
    license:
      name: "MIT License"         # License name (default: "MIT License")
      url: "https://opensource.org/licenses/MIT" # License URL
    servers:
      - url: "http://localhost:8080"  # Server URL
        description: "Local Development Server" # Server description
```

### Security/JWT

```yaml
starter-kit:
  security:
    enabled: true                 # Enable/disable security (default: true)
    jwt:
      secret-key: "${JWT_SECRET:your-secret-key}" # JWT secret key (required)
      token-expiration-ms: 3600000 # Token expiration in milliseconds (default: 1 hour)
      issuer: "spring-boot-starter-kit" # Token issuer (default: "spring-boot-starter-kit")
      token-prefix: "Bearer "     # Token prefix (default: "Bearer ")
      header-name: "Authorization" # Header name (default: "Authorization")
    auth:
      public-paths:               # Paths that don't require authentication
        - "/api/auth/**"
        - "/api/public/**"
        - "/swagger-ui/**"
        - "/v3/api-docs/**"
      stateless: true             # Use stateless sessions (default: true)
      csrf-enabled: false         # Enable CSRF protection (default: false)
```

### Logging

```yaml
starter-kit:
  logging:
    enabled: true                 # Enable/disable logging features (default: true)
    mdc:
      enabled: true               # Enable MDC context tracking (default: true)
      request-id-key: "requestId" # MDC key for request IDs (default: "requestId")
      user-id-key: "userId"       # MDC key for user IDs (default: "userId")
      include-client-ip: true     # Include client IP in MDC (default: true)
      include-user-roles: false   # Include user roles in MDC (default: false)
    request:
      enabled: true               # Enable request logging (default: true)
      include-headers: true       # Include headers in logs (default: true)
      include-payload: true       # Include request/response bodies in logs (default: true)
      max-payload-length: 10000   # Maximum payload length to log (default: 10000)
      exclude-paths:              # Paths to exclude from logging
        - "/actuator/**"
        - "/swagger-ui/**"
        - "/v3/api-docs/**"
```

### Web Configuration

```yaml
starter-kit:
  web-config:
    enabled: true                 # Enable/disable web configuration (default: true)
    cors:
      enabled: true               # Enable CORS (default: true)
      allowed-origins: ["*"]      # Allowed origins (default: ["*"])
      allowed-methods: ["GET", "POST", "PUT", "DELETE", "OPTIONS"] # Allowed methods
      allowed-headers: ["*"]      # Allowed headers (default: ["*"])
      allow-credentials: true     # Allow credentials (default: true)
      max-age: 3600               # Max age in seconds (default: 3600)
    compression:
      enabled: true               # Enable response compression (default: true)
      min-response-size: 2048     # Minimum size to compress (default: 2048 bytes)
      mime-types:                 # MIME types to compress
        - "text/html"
        - "text/xml"
        - "text/plain"
        - "text/css"
        - "text/javascript"
        - "application/javascript"
        - "application/json"
        - "application/xml"
```

### Persistence

```yaml
starter-kit:
  base-entity:
    enabled: true                 # Enable/disable persistence support (default: true)
    enable-auditing: true         # Enable entity auditing (default: true)
```

## 📊 API Response Format

The starter kit provides a standardized API response format for all endpoints:

```json
{
  "success": true,
  "data": {
    // Your response data
  },
  "timestamp": "2023-06-15T10:30:45.123Z",
  "meta": {
    // Optional metadata
  }
}
```

For paginated responses:

```json
{
  "success": true,
  "data": {
    "content": [
      // Your page content
    ],
    "totalElements": 100,
    "totalPages": 10,
    "page": 0,
    "size": 10,
    "first": true,
    "last": false
  },
  "timestamp": "2023-06-15T10:30:45.123Z",
  "meta": {
    "totalElements": 100,
    "totalPages": 10,
    "page": 0,
    "size": 10
  }
}
```

For error responses:

```json
{
  "success": false,
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "User not found with id: 123",
    "detail": "Additional error details"
  },
  "timestamp": "2023-06-15T10:30:45.123Z"
}
```

### Using the Response Builder

The starter kit provides a convenient `ResponseEntityBuilder` to create standardized responses:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntityBuilder.success(user);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsers(Pageable pageable) {
        Page<UserDTO> users = userService.findAll(pageable);
        return ResponseEntityBuilder.page(users);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO user) {
        UserDTO created = userService.create(user);
        return ResponseEntityBuilder.created(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntityBuilder.noContent();
    }
}
```

## 🚨 Exception Handling

The starter kit provides a comprehensive exception handling framework:

### Standard Exception Classes

- `ResourceNotFoundException` - For 404 Not Found responses
- `BadRequestException` - For 400 Bad Request responses
- `UnauthorizedException` - For 401 Unauthorized responses
- `ForbiddenException` - For 403 Forbidden responses
- `ConflictException` - For 409 Conflict responses

### Using Custom Exceptions

```java
@Service
public class UserService {
    
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public UserDTO create(UserDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("User already exists with email: " + userDto.getEmail());
        }
        // Create user logic
    }
}
```

### Internationalization Support

To enable i18n for error messages:

1. Set `starter-kit.exception-handling.enable-i18n` to `true`
2. Create a `messages/error-messages.properties` file (and locale variants)

```properties
# messages/error-messages.properties
resource.notfound=Resource not found
resource.notfound.user=User not found with {0}: {1}
```

## 📝 Base Entities

The starter kit provides base entity classes to simplify entity creation:

### Using BaseEntity (with Long ID)

```java
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    
    private String name;
    private BigDecimal price;
    
    // Getters and setters
}
```

### Using UuidEntity (with UUID ID)

```java
@Entity
@Table(name = "orders")
public class Order extends UuidEntity {
    
    private LocalDate orderDate;
    private String status;
    
    // Getters and setters
}
```

### Auditing Fields

Both base entities include the following auditing fields:

- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp
- `createdBy` - User who created the entity
- `updatedBy` - User who last updated the entity
- `version` - Optimistic locking version

## 🔒 Security & JWT

The starter kit provides a ready-to-use JWT authentication system:

### Implementing Authentication

1. Create a user details service:

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
```

2. Create an authentication controller:

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // Generate token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(userDetails);
        
        // Return response
        AuthResponse response = new AuthResponse(token);
        return ResponseEntityBuilder.success(response);
    }
}
```

## 📋 API Documentation

The starter kit integrates Swagger/OpenAPI for API documentation:

### OpenAPI Annotations

Use the provided annotations to document your API:

#### DocumentPage Documentation

```java
@RestController
@RequestMapping("/api/users")
@ApiResponseWrapper // Documents standard responses (200, 400, 401, 403, 404, 500)
public class UserController {
    
    @GetMapping
    @ApiPageable // Documents pageable parameters (page, size, sort)
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsers(Pageable pageable) {
        // Implementation
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        // Implementation
    }
}
```

#### API Versioning

```java
@RestController
@RequestMapping("/api/v1/users")
@ApiVersion("v1") // Tags the API with a version
public class UserControllerV1 {
    // Implementation
}

@RestController
@RequestMapping("/api/v2/users")
@ApiVersion("v2") // Tags the API with a version
public class UserControllerV2 {
    // Implementation
}
```

## 📄 Logging

The starter kit provides enterprise-grade logging:

### MDC Context

The MDC filter automatically adds the following to your logging context:

- Request ID - Unique identifier for each request
- User ID - Authenticated user identifier (when available)
- Session ID - HTTP session identifier
- Client IP - Originating IP address

### Request/Response Logging

The request logging filter logs:

- Request method, URI, and query parameters
- Request headers (optional)
- Request body (optional)
- Response status and timing
- Response headers (optional)
- Response body (optional)

### Customizing Logging

1. Configure log levels in `application.yml`:

```yaml
logging:
  level:
    com.bensamir.starter: DEBUG  # Set the log level for starter kit components
    your.package: INFO           # Set the log level for your components
```

2. Use the MDC context in your service classes:

```java
@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    public void createUser(UserDTO userDTO) {
        // MDC context (request ID, user ID, etc.) is automatically available
        log.info("Creating user with email: {}", userDTO.getEmail());
        // Implementation
        log.info("User created successfully");
    }
}
```

## 📚 Examples

### Complete Controller Example

```java
@RestController
@RequestMapping("/api/users")
@ApiResponseWrapper
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @ApiPageable
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsers(Pageable pageable) {
        Page<UserDTO> users = userService.findAll(pageable);
        return ResponseEntityBuilder.page(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.findById(id);
            return ResponseEntityBuilder.success(user);
        } catch (ResourceNotFoundException e) {
            // No need to catch this exception as it's handled globally
            throw e;
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody UserDTO userDTO, 
            BindingResult bindingResult) {
        
        // Validation errors are handled by the global exception handler
        UserDTO created = userService.create(userDTO);
        return ResponseEntityBuilder.created(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserDTO userDTO) {
        
        UserDTO updated = userService.update(id, userDTO);
        return ResponseEntityBuilder.success(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntityBuilder.noContent();
    }
}
```

### Service Example

```java
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
    
    @Override
    public UserDTO create(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ConflictException("User already exists with email: " + userDTO.getEmail());
        }
        
        User user = userMapper.toEntity(userDTO);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
    
    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        User user = userMapper.toEntity(userDTO);
        user.setId(id);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
    
    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
    }
}
```

### Entity Example

```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Getters and setters
}
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.
