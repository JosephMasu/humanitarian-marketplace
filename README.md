# Humanitarian Marketplace

Java 21 / Spring Boot microservices in one Maven monorepo. Architecture rules are in
[`CLAUDE.md`](CLAUDE.md); decisions are in [`docs/adr/`](docs/adr/).

| Service                | Port (local) | Owns                                              |
|------------------------|--------------|---------------------------------------------------|
| `api-gateway`          | 8080         | Routing, rejecting requests without a valid token |
| `user-service`         | 8081         | User profiles, keyed by Keycloak subject          |
| `organization-service` | 8082         | Organizations and their members                   |
| Keycloak               | 8180         | Login, registration, tokens, platform roles       |
| PostgreSQL             | 5432         | One database per service                          |
| Kafka (KRaft)          | 9092         | Events between services                           |

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (Compose v2) — also needed for tests, which use Testcontainers

## Run everything

```bash
docker compose up --build
```

Or run only the infrastructure and start services from your IDE:

```bash
docker compose up postgres kafka keycloak
```

Keycloak admin console: http://localhost:8180 (admin / admin).
Dev users in the `humanitarian-marketplace` realm: `dev-ngo` and `dev-admin`, password `dev-password`.

### Call the API with a token

```bash
TOKEN=$(curl -s http://localhost:8180/realms/humanitarian-marketplace/protocol/openid-connect/token \
  -d grant_type=password -d client_id=marketplace-web \
  -d username=dev-ngo -d password=dev-password | jq -r .access_token)

curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/organizations
```

The password grant is enabled on `marketplace-web` for local testing only. Turn it off in any shared environment.

## Build and test

```bash
mvn verify
```

Each service has:
- Swagger UI at `/swagger-ui.html` and the OpenAPI spec at `/v3/api-docs`
- Health at `/actuator/health`
- Prometheus metrics at `/actuator/prometheus`

## Layout

```
services/          one Maven module per service
infrastructure/    docker/ (shared Dockerfile), postgres/, keycloak/
docs/adr/          architecture decision records
.claude/skills/    agent skills (mattpocock/skills)
```
