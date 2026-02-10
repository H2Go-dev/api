# H2Go API - Docker & CI/CD Setup

This document provides instructions for running the H2Go API.

## Quick Start

1. **Build and run the application:**
   ```bash
   make docker-build
   make docker-run
   ```

3. **Check application health:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

4. **Access the API:**
   - Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Health Check: `http://localhost:8080/actuator/health`

## Make Commands

```bash
# Show all available commands
make help

# Build the project
make build

# Run tests
make test

# Run style checking
make style-check

# Run security scanning
make security-scan

# Run smoke tests
make smoke-test

# Run full CI pipeline locally
make ci-local
```

