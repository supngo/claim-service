# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew build          # Full build with tests and coverage
./gradlew bootRun        # Start the application (requires Redis on localhost:6379)
./gradlew test           # Run all tests
./gradlew clean          # Clean build artifacts
./gradlew jacocoTestReport  # Generate coverage report → build/reports/jacoco/test/html/
```

**Run a single test class or method:**
```bash
./gradlew test --tests="ClaimControllerTest"
./gradlew test --tests="ClaimControllerTest.getClaimById_returns200_whenFound"
```

## Prerequisites

Redis must be running locally on `localhost:6379` before starting the application. The app runs on port **8081**.

## Architecture

Layered Spring Boot service (`com.naturecode.claim_service`):

```
controller  →  service  →  repository
                 ↕
              mapper (MapStruct)
```

- **Controller** — REST endpoints: `GET /claims/{claimId}`, `GET /claims?customerId=X`, `PUT /claims/{claimId}`
- **Service** — Business logic + Redis cache management via `@Cacheable` / `@CacheEvict`
- **Repository** — `InMemoryClaimRepository` backed by `ConcurrentHashMap`, pre-seeded with 5 claims (CLM-001–CLM-005)
- **Mapper** — MapStruct interface converting between `Claim` (model) and `ClaimRequest`/`ClaimResponse` (DTOs)

## Caching

Two named caches backed by Redis (10-minute TTL):
- `claims` — keyed by `claimId`
- `claimsByCustomer` — keyed by `customerId`

On `PUT` update, the service uses `@Caching` with multiple `@CacheEvict` annotations to invalidate both caches simultaneously.

## Testing Approach

- **`@WebMvcTest` + MockMvc** for controller tests (mocks `ClaimService`)
- **Plain unit tests + Mockito** for service and repository tests
- JaCoCo excludes the main application class, all model classes, all DTOs, and the repository interface from coverage requirements

## Key Tech

- Java 21, Spring Boot 4.0.5, Gradle 9.4.1
- MapStruct 1.6.3 (compile-time code generation) + Lombok
- Spring Data Redis + Spring Cache abstraction
- Spring Actuator exposes `/actuator/caches` endpoint
