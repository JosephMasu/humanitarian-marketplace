# Humanitarian Marketplace

Microservices monorepo. Services live under `services/`, shared infra under `infrastructure/`, docs under `docs/` (`architecture/`, `api/`, `events/`).

## Engineering standards (non-negotiable)

| Standard       | Decision        |
|----------------|-----------------|
| Java           | 21              |
| Build tool     | Maven           |
| Framework      | Spring Boot     |
| API style      | REST            |
| Communication  | REST + Kafka    |
| Database       | PostgreSQL      |
| Authentication | Keycloak        |
| Containers     | Docker          |
| Architecture   | Microservices   |
| Repository     | Monorepo        |

## Technology stack

| Component               | Technology                         |
|-------------------------|------------------------------------|
| Language                | Java 21+                           |
| Framework               | Spring Boot                        |
| Microservices ecosystem | Spring Cloud (Gateway, OpenFeign)  |
| API Gateway             | Spring Cloud Gateway               |
| Service discovery       | Kubernetes / Compose DNS (ADR-0001)|
| Central configuration   | Env vars, ConfigMaps, Secrets (ADR-0001) |
| Authentication          | Keycloak + Spring Security         |
| Sync communication      | REST + OpenFeign                   |
| Messaging               | Apache Kafka                       |
| Databases               | PostgreSQL                         |
| ORM                     | Spring Data JPA / Hibernate        |
| Migrations              | Flyway                             |
| File storage            | MinIO / Amazon S3                  |
| Caching                 | Redis                              |
| Search                  | OpenSearch                         |
| AI integration          | Spring AI                          |
| API documentation       | SpringDoc OpenAPI                  |
| Monitoring              | Prometheus + Grafana               |
| Distributed tracing     | OpenTelemetry + Jaeger             |
| Containerization        | Docker                             |
| Orchestration           | Kubernetes                         |
| Testing                 | JUnit 5 + Testcontainers           |

## Conventions for agents

- Decisions live in `docs/adr/`. Read them before changing architecture.
- No Eureka, no Config Server (ADR-0001). Services call each other by DNS name (`http://<service>:8080`), with Feign base URLs from properties. Spring profiles: `local`, `test`, `k8s`.
- Keycloak owns authentication, credentials and platform roles (ADR-0002). `user-service` stores profiles only, keyed by the JWT `sub`. Org membership lives in `organization-service`. Every service validates JWTs itself.

- Don't introduce libraries or tools outside the stack above without an ADR in `docs/adr/`.
- Each service owns its own PostgreSQL schema/database; no cross-service DB access. Services talk via REST (OpenFeign) for queries and Kafka for events.
- Schema changes go through Flyway migrations (`src/main/resources/db/migration`), never `ddl-auto=update`.
- Every service exposes OpenAPI via SpringDoc, Actuator + Prometheus metrics, and OpenTelemetry tracing.
- Document Kafka topics and event payloads in `docs/events/`; REST contracts in `docs/api/`.
- Build with Maven 3.9+ (`mvn verify`) from the root aggregator POM. (Maven Wrapper not added yet.)
- Tests: JUnit 5; integration tests use Testcontainers (PostgreSQL, Kafka, Keycloak, Redis) — no H2 or in-memory substitutes.
- Some installed skills (e.g. `tdd`, `prototype`, `codebase-design`) have TypeScript examples. Apply the same principles in idiomatic Java/Spring.

## Agent skills

Matt Pocock's skills are installed in `.claude/skills/` (see `SOURCE-mattpocock-skills.txt`).
Run `/setup-matt-pocock-skills` once to configure the issue tracker, triage labels and domain docs; it appends its config here.
