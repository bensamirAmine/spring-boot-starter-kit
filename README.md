# Spring Boot Starter Kit

A comprehensive collection of reusable components for Spring Boot applications that provides standardized implementations of common patterns and reduces boilerplate code.

## Features

The starter kit includes the following components, each of which can be enabled or disabled independently:

### Core Features

#### 1. Exception Handling

Provides a standardized way to handle and respond to exceptions across your application.

- Global exception handler with consistent error responses
- Standard exception types (ResourceNotFoundException, BadRequestException, etc.)
- Validation error formatting
- Customizable error messages
- Internationalization (i18n) support
- Error logging with configurable levels

#### 2. Base Persistence Layer

Includes base entity classes and auditing support for JPA.

- Common fields (id, created_at, updated_at, version)
- Support for both numeric and UUID identifiers
- Automatic auditing of creation and modification timestamps
- Optimistic locking with version tracking
- Utility methods for entity operations

#### 3. Response Utilities

Standardizes API responses for a consistent experience.

- Unified response format for all API endpoints
- Pagination support with metadata
- Success/error response helpers
- Fluent builder API for creating responses

#### 4. Web Configuration

Provides sensible defaults for web-related configuration.

- CORS configuration with extensive customization options
- Response compression
- Character encoding
- Common HTTP headers

#### 5. Security Framework

Complete authentication and authorization solution.

- JWT token-based authentication
- Role-based access control
- Custom security annotations (@RequiresAdmin, @RequiresUser)
- Integration with Spring Security
- Configurable public/private endpoints

#### 6. Base Controllers

Standardized CRUD operations for RESTful APIs.

- Abstract controllers with common REST endpoints
- Entity/DTO mapping support
- Pagination handling
- Integration with exception handling and response utilities

#### 7. API Documentation

Automatic OpenAPI documentation generation.

- Swagger UI integration
- Customizable API information
- API versioning support
- Pre-configured documentation groups

#### 8. Logging Configuration

Centralized logging with context tracking.

- Request/response logging
- Performance monitoring
- MDC context for request tracking
- Structured JSON logging

### Upcoming Features (Roadmap)

#### 9. Advanced Caching

- Multi-level cache configuration
- Cache key generation
- TTL management
- Cache invalidation strategies

#### 10. Event Publishing

- Domain event publishing
- Event listeners and handlers
- Asynchronous event processing

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.bensamir</groupId>
    <artifactId>spring-boot-starter-kit</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

### Basic Usage

Most features work automatically with sensible defaults. Simply add the dependency to your project, and the components will be auto-configured.

### Configuration

All components can be configured using properties in your `application.yml` or `application.properties` file:

```yaml
starter-kit:
  # Exception handling configuration
  exception-handling:
    enabled: true
    log-exceptions: true
    include-stack-trace: false
    enable-i18n: false
    default-messages:
      resource.notfound: "Resource not found"
      bad.request: "Bad request"
      validation.error: "Validation error"
      internal.error: "An unexpected error occurred"

  # Base entity configuration
  base-entity:
    enabled: true
    enable-auditing: true
    id-type: "LONG"  # LONG or UUID

  # Response utilities configuration
  response:
    enabled: true
    include-timestamp: true

  # Web configuration
  web-config:
    enabled: true
    cors:
      enabled: true
      allowed-origins:
        - "*"
      allowed-methods:
        - "GET"
        - "POST"
        - "PUT"
        - "DELETE"
        - "OPTIONS"
      allowed-headers:
        - "*"
      allow-credentials: true
      max-age: 3600
    compression:
      enabled: true
      min-response-size: "2KB"
      mime-types:
        - "text/html"
        - "text/xml"
        - "text/plain"
        - "text/css"
        - "text/javascript"
        - "application/javascript"
        - "application/json"
  
  # Security configuration
  security:
    enabled: true
    jwt:
      secret-key: "your-very-secure-secret-key-here"
      access-token-expiration-ms: 900000  # 15 minutes
      refresh-token-expiration-ms: 2592000000  # 30 days
      issuer: "spring-boot-starter-kit"
    auth:
      public-paths:
        - "/api/auth/**"
        - "/api/public/**"
        - "/swagger-ui/**"
        - "/v3/api-docs/**"
      stateless: true
      csrf-enabled: false
  
  # Base controllers configuration
  controllers:
    enabled: true
    default-page-size: "20"
    default-sort-direction: "asc"
  
  # API documentation configuration
  api-docs:
    enabled: true
    title: "API Documentation"
    description: "API Documentation generated by Spring Boot Starter Kit"
    version: "1.0"
    contact:
      name: "API Support"
      email: "support@example.com"
    servers:
      - url: "http://localhost:8080"
        description: "Local Development Server"
  
  # Logging configuration
  logging:
    enabled: true
    request:
      enabled: true
      include-headers: true
      include-payload: true
      exclude-paths:
        - "/actuator/**"
        - "/swagger-ui/**"
        - "/v3/api-docs/**"
    performance:
      enabled: true
      slow-execution-threshold-ms: 1000
      include-packages:
        - "com.example"
    mdc:
      enabled: true
      request-id-key: "requestId"
      user-id-key: "userId"
    json:
      enabled: true
      include-logger-name: true
      include-thread-name: true
```

## Component Usage Examples

### Exception Handling

1. Use predefined exceptions in your code:

```java
// Throw a not found exception
throw new ResourceNotFoundException("User", "id", userId);

// Throw a bad request exception
throw new BadRequestException("Invalid input data");
```

2. Create custom exceptions by extending `BaseException`:

```java
public class PaymentFailedException extends BaseException {
    public PaymentFailedException(String message) {
        super(message, "PAYMENT_FAILED");
    }
}
```

3. Customizing error messages with i18n:

```
# messages/error-messages_en.properties
resource.notfound=Resource not found
bad.request=Bad request

# messages/error-messages_fr.properties
resource.notfound=Ressource non trouvée
bad.request=Requête invalide
```

### Base Persistence Layer

1. Create entities by extending the base classes:

```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;
    
    private String name;
    
    // Getters and setters
}
```

2. For UUID-based entities:

```java
@Entity
@Table(name = "orders")
public class Order extends BaseUuidEntity {
    @ManyToOne
    private User user;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    // Getters and setters
}
```

### Response Utilities

Use the `ResponseEntityBuilder` to create standardized responses:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return ResponseEntityBuilder.success(user);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody @Valid UserDto userDto) {
        UserDto createdUser = userService.create(userDto);
        return ResponseEntityBuilder.created(createdUser);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDto>>> getAllUsers(Pageable pageable) {
        Page<UserDto> users = userService.findAll(pageable);
        return ResponseEntityBuilder.page(users);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntityBuilder.noContent();
    }
}
```

### Security Framework

1. Implement the `SecuredUser` interface in your user entity:

```java
@Entity
@Table(name = "users")
public class User extends BaseEntity implements SecuredUser {
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();
    
    @Column(nullable = false)
    private boolean enabled = true;
    
    // Getters and setters
    
    @Override
    public String getId() {
        return this.getId().toString();
    }
    
    @Override
    public String getUsername() {
        return this.email;
    }
    
    @Override
    public Set<String> getRoles() {
        return this.roles;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
```

2. Implement a `SecurityUserService`:

```java
@Service
public class CustomUserService implements SecurityUserService {
    
    private final UserRepository userRepository;
    
    public CustomUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
        return UserPrincipal.from(user);
    }
    
    @Override
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        
        return UserPrincipal.from(user);
    }
}
```

3. Use security annotations to protect resources:

```java
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @GetMapping("/users")
    @RequiresAdmin
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        // Only accessible to users with ADMIN role
        List<UserDto> users = userService.findAll();
        return ResponseEntityBuilder.success(users);
    }
}

@RestController
@RequestMapping("/api/user")
public class ProfileController {
    
    @GetMapping("/profile")
    @RequiresUser
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(Authentication authentication) {
        // Accessible to users with USER role
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDto userDto = userService.findById(userPrincipal.getId());
        return ResponseEntityBuilder.success(userDto);
    }
}
```

4. Authentication endpoints:

The security module provides these authentication endpoints:

- **Login**: `POST /api/auth/login`
  ```json
  {
    "username": "user@example.com",
    "password": "password"
  }
  ```

- **Refresh Token**: `POST /api/auth/refresh`
  ```json
  {
    "refreshToken": "your-refresh-token"
  }
  ```

These endpoints return a response with access and refresh tokens:

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 1621436800000
  },
  "timestamp": "2023-05-20T10:30:45Z"
}
```

### Base Controllers

1. Create a service implementation that extends `JpaCrudService`:

```java
@Service
public class ProductService extends JpaCrudService<Product, Long> {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }
    
    // Add custom service methods here
}
```

2. Create a mapper for converting between entities and DTOs:

```java
@Component
public class ProductMapper implements EntityMapper<Product, ProductDto> {
    
    @Override
    public ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        return dto;
    }
    
    @Override
    public Product toEntity(ProductDto dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        return entity;
    }
    
    @Override
    public List<ProductDto> toDtoList(List<Product> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product updateEntityFromDto(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
```

3. Create a controller that extends `BaseCrudController`:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseCrudController<Product, ProductDto, Long> {
    
    private final ProductService productService;
    private final ProductMapper productMapper;
    
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }
    
    @Override
    protected CrudService<Product, Long> getService() {
        return productService;
    }
    
    @Override
    protected EntityMapper<Product, ProductDto> getMapper() {
        return productMapper;
    }
    
    @Override
    protected String getEntityName() {
        return "Product";
    }
    
    // Add custom controller methods here
}
```

4. For simpler controllers, use the `SimplifiedCrudController`:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends SimplifiedCrudController<Product, ProductDto, Long> {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @Override
    protected CrudService<Product, Long> getService() {
        return productService;
    }
    
    // That's it! No mapper needed - it's auto-detected and created
}
```

### API Documentation

1. Document your controllers with OpenAPI annotations:

```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management API")
public class ProductController extends BaseCrudController<Product, ProductDto, Long> {
    
    @Override
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID")
    @ApiResponseWrapper
    public ResponseEntity<ApiResponse<ProductDto>> findById(@PathVariable Long id) {
        return super.findById(id);
    }
    
    @Override
    @Operation(summary = "Get all products", description = "Retrieves a paginated list of products")
    @ApiPageable
    @ApiResponseWrapper
    public ResponseEntity<ApiResponse<PageResponse<ProductDto>>> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }
    
    // Other methods...
}
```

2. Access the API documentation:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Logging

1. Use the `@LogExecution` annotation to log method entry and exit:

```java
@Service
public class ProductService extends JpaCrudService<Product, Long> {
    
    @LogExecution
    public Product findById(Long id) {
        // Method execution will be logged with parameters and return value
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
    
    @LogExecution(logParameters = false, logReturnValue = false)
    public void deleteProduct(Long id) {
        // Only method entry and exit will be logged, without parameters
        productRepository.deleteById(id);
    }
}
```

2. Access context information in logs:

```java
@Service
public class UserService {
    
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @LogExecution
    public User authenticateUser(String username, String password) {
        // The request ID and other MDC values will automatically be included in the log
        logger.info("Authenticating user: {}", username);
        
        // ... authentication logic
        
        return user;
    }
}
```

## Extending the Starter Kit

### Creating Custom Exception Handlers

You can add your own exception handlers by creating a class with `@ControllerAdvice` and implementing `@ExceptionHandler` methods:

```java
@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ApiError> handlePaymentFailedException(
            PaymentFailedException ex, HttpServletRequest request) {
        
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_GATEWAY.value());
        apiError.setErrorCode("PAYMENT_FAILED");
        apiError.setError("Payment Gateway Error");
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_GATEWAY);
    }
}
```

### Overriding Auto-Configuration

You can override any auto-configured bean by defining your own bean with the same name:

```java
@Configuration
public class CustomConfig {
    
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(
            StarterKitProperties properties,
            ErrorMessageResolver messageResolver) {
        return new CustomGlobalExceptionHandler(properties, messageResolver);
    }
}
```

### Custom Entity Mappers

For complex mapping scenarios, you can use MapStruct:

```java
@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapStructMapper<Product, ProductDto> {
    // MapStruct automatically implements all methods
    
    // Add custom mappings for complex cases
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tagsCount", expression = "java(product.getTags().size())")
    ProductDto toDto(Product product);
    
    // For nested objects
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto dto);
    
    // Custom methods for complex mappings
    default void mapCategory(ProductDto dto, @MappingTarget Product product) {
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
    }
}
```

## Roadmap

### Future Components

1. **Advanced Caching Support** (Q3 2025)
    - Multi-level cache configuration
    - Cache key generation
    - TTL management
    - Cache invalidation strategies

2. **Event Publishing** (Q4 2025)
    - Domain event publishing
    - Event listeners and handlers
    - Asynchronous event processing

3. **Integration Testing Support** (Q1 2026)
    - Test utilities and helpers
    - In-memory database configuration
    - Mock service implementations

## Contributing

Contributions are welcome! If you'd like to contribute, please:

1. Fork the repository
2. Create a feature branch
3. Add your changes
4. Submit a pull request

For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License - see the LICENSE file for details.