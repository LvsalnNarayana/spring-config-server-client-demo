
# Spring-Config-Server-Client-Demo

## Overview

This project is a **multi-microservice demonstration** of **Spring Cloud Config Server** and **Config Clients** in **Spring Boot 3.x**. It showcases **centralized configuration management** — a foundational pattern in microservices architectures for managing application properties across multiple services and environments without redeployment.

The focus is on **production best practices**: Git-backed configuration, client dynamic refresh, encryption/decryption, failover, and security.

## Real-World Scenario (Simulated)

In large-scale microservices ecosystems like **Netflix OSS** (Eureka, Ribbon, Hystrix era):
- Hundreds of services run in multiple environments (dev, staging, prod).
- Configuration (DB URLs, feature flags, API keys) changes frequently.
- Hardcoding properties or rebuilding JARs for config changes is inefficient and error-prone.
- Centralized config enables instant propagation of changes across the fleet.

We simulate a typical e-commerce setup with a **Config Server** backed by a Git repository and multiple client services (Product, Order, Notification) that pull their configuration dynamically.

## Microservices Involved

| Service                  | Responsibility                                                                 | Port  |
|--------------------------|--------------------------------------------------------------------------------|-------|
| **config-server**        | Central Config Server: serves properties from Git repository                   | 8888  |
| **eureka-server**        | Service discovery (Netflix Eureka) — clients register here                     | 8761  |
| **product-service**      | Sample client: manages products, pulls config from server                      | 8081  |
| **order-service**        | Sample client: handles orders, uses feature flags from central config          | 8082  |
| **notification-service** | Sample client: sends notifications, demonstrates encryption/decryption        | 8083  |

## Tech Stack

- Spring Boot 3.x
- Spring Cloud Config Server & Client
- Git backend (local or remote repository)
- Spring Cloud Bus (optional for broadcast refresh)
- Spring Boot Actuator (/refresh endpoint)
- Spring Security (optional config server auth)
- Spring Cloud Netflix Eureka
- Lombok
- Maven (multi-module)
- Docker & Docker Compose

## Docker Containers

```yaml
services:
  config-server:
    build: ./config-server
    ports:
      - "8888:8888"
    environment:
      - GIT_URI=https://github.com/your-repo/config-repo.git  # or local file://

  eureka-server:
    build: ./eureka-server
    ports:
      - "8761:8761"

  product-service:
    build: ./product-service
    depends_on:
      - config-server
      - eureka-server
    ports:
      - "8081:8081"

  order-service:
    build: ./order-service
    depends_on:
      - config-server
      - eureka-server
    ports:
      - "8082:8082"

  notification-service:
    build: ./notification-service
    depends_on:
      - config-server
      - eureka-server
    ports:
      - "8083:8083"
```

Run with: `docker-compose up --build`

## Configuration Strategy

| Feature                        | Implementation Details                                                  |
|--------------------------------|-------------------------------------------------------------------------|
| **Git Backend**                | Multiple YAML files: `application.yml`, `{service-name}.yml`, profiles  |
| **Profiles**                   | `dev`, `prod` — clients use `spring.profiles.active`                    |
| **Client Bootstrap**           | `bootstrap.yml` with `spring.cloud.config.uri=http://config-server:8888`|
| **Dynamic Refresh**            | `@RefreshScope` on beans → POST `/actuator/refresh` triggers reload     |
| **Encryption/Decryption**      | `{cipher}` prefix in properties → config server decrypts on fetch      |
| **Fail Fast / Failover**       | Client fails fast if config server down (or fallback to local)         |
| **Search Paths**               | Multiple repos or paths configurable                                   |

## Key Features

- Centralized Git-backed configuration
- Environment-specific profiles (dev/prod)
- Dynamic property refresh without restart
- Encrypted sensitive properties (passwords, API keys)
- Client failover and bootstrap behavior
- Service-specific and shared properties
- Integration with Eureka discovery
- Secure config server (optional basic auth)

## Config Repository Structure (Example)

```
config-repo/
├── application.yml          # shared by all clients
├── product-service.yml
├── product-service-dev.yml
├── product-service-prod.yml
├── order-service.yml
├── notification-service.yml
└── encrypted.properties     # with {cipher} values
```

### Sample Property
```yaml
# In product-service-prod.yml
database:
  url: jdbc:postgresql://prod-db:5432/products
  password: '{cipher}AQ...encrypted...'
feature:
  new-ui: true
```

## Expected Endpoints

### Config Server (`http://localhost:8888`)

| Endpoint                              | Description                                      |
|---------------------------------------|--------------------------------------------------|
| GET `/{application}/{profile}`        | Raw config (e.g., `/product-service/prod`)      |
| GET `/{application}-{profile}.yml`    | YAML format                                      |
| GET `/encrypted/decrypt`              | POST with encrypted text to decrypt              |

### Client Services (e.g., Product Service `http://localhost:8081`)

| Endpoint                              | Description                                      |
|---------------------------------------|--------------------------------------------------|
| GET `/api/config/db-url`              | Expose current config value                      |
| POST `/actuator/refresh`              | Trigger dynamic refresh of @RefreshScope beans   |
| GET `/actuator/env`                   | Full environment with resolved config            |

## Architecture Overview

```
Git Repository (config-repo)
   ↓
Config Server → reads on demand
   ↓
Clients (bootstrap phase)
   ↓ (spring.cloud.config.uri)
Product / Order / Notification Services
   ↓ (@RefreshScope)
Dynamic refresh via /actuator/refresh
   ↓
Eureka Discovery
```

**Config Flow**:
1. Client starts → reads `bootstrap.yml` → contacts Config Server
2. Server fetches from Git → merges `application.yml` + service-specific + profile
3. Client loads properties → application starts
4. Change in Git → POST `/actuator/refresh` → reload @RefreshScope beans

## How to Run

1. Clone repository
2. Set up Git repo (or use local file:// path)
3. Start Docker: `docker-compose up --build`
4. Access Eureka: `http://localhost:8761`
5. Access Config Server: `http://localhost:8888/product-service/dev`
6. Call client endpoint → see centralized config
7. Update Git property → POST `/actuator/refresh` on client → new value applied

## Testing Centralized Config

1. Start services → config from Git
2. Change feature flag in Git → refresh client → behavior changes
3. Use different profiles → different DB URLs
4. Encrypt property → client receives decrypted value
5. Stop config server after startup → client continues (cached)
6. Fail fast on startup if config server down

## Skills Demonstrated

- Spring Cloud Config Server setup (Git, encryption)
- Client configuration with bootstrap.yml
- Dynamic refresh with @RefreshScope
- Profile-based and service-specific properties
- Encrypted properties handling
- Production concerns: failover, security
- Integration with service discovery

## Future Extensions

- Spring Cloud Bus for broadcast refresh
- Vault or Consul backend
- Config server high availability
- Encrypted Git repo with symmetric key
- Client-side config fallback
- Integration with Kubernetes ConfigMaps/Secrets

