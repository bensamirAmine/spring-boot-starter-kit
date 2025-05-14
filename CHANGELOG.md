# Changelog

All notable changes to the Spring Boot Starter Kit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - Initial Release

### Added
- Exception handling framework with standardized error responses
  - Global exception handler
  - Standard exception types (ResourceNotFoundException, BadRequestException)
  - Internationalization support for error messages
  - Validation error formatting

- Base persistence layer for JPA entities
  - BaseEntity with Long ID
  - BaseUuidEntity with UUID ID
  - Automatic auditing with creation and modification timestamps
  - Optimistic locking with version field

- Response utilities for standardized API responses
  - ApiResponse wrapper class
  - Pagination support with metadata
  - ResponseEntityBuilder for fluent API

- Web configuration with sensible defaults
  - CORS configuration
  - Response compression
  - Character encoding settings

- Security framework with JWT authentication
  - JWT token generation and validation
  - Role-based access control
  - Custom security annotations (@RequiresAdmin, @RequiresUser)
  - Authentication endpoints (/api/auth/login, /api/auth/refresh)

- Base controllers for CRUD operations
  - BaseCrudController with standard endpoints
  - ReadOnlyController and WriteOnlyController variants
  - Entity/DTO mapping support
  - Simplified controller with auto-mapping

- API documentation with OpenAPI/Swagger
  - Customizable API information
  - API versioning support
  - Helper annotations for common documentation tasks

- Logging configuration with MDC context
  - Request/response logging
  - Performance monitoring
  - Method execution logging
  - Structured JSON logging

### Planned for Future Releases
- Advanced caching support
- Event publishing framework
- Integration testing utilities