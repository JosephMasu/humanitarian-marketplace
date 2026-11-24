# ADR-0001: Use platform DNS and environment config instead of Eureka and Spring Cloud Config

- Status: Accepted
- Date: 2026-11-24

## Context

The initial stack listed Netflix Eureka (service discovery) and Spring Cloud Config (central configuration). The target runtime is Kubernetes, which already provides both:

- **Discovery:** every Kubernetes `Service` gets a stable DNS name and load-balances across pods.
- **Configuration:** `ConfigMap` and `Secret` objects inject config as environment variables or files.

Running Eureka and Config Server on Kubernetes means two discovery systems and two config systems, plus two extra services to deploy, secure, monitor and keep highly available. Eureka's view can also drift from Kubernetes' view of which pods are healthy.

Docker Compose (local dev) gives the same DNS-by-service-name behaviour as Kubernetes.

## Decision

- **No Eureka and no Config Server.**
- Services address each other by DNS name: `http://organization-service:8080`. That works unchanged in Docker Compose and Kubernetes.
- The API gateway routes with static URIs, using those DNS names (e.g. `uri: http://organization-service:8080`).
- OpenFeign clients take their base URL from a property (e.g. `clients.organization.url`). There's no client-side load balancer; the Kubernetes Service does the load balancing.
- Configuration follows Spring Boot's normal layering:
  - `application.yml` holds safe defaults.
  - Environment variables override them: `.env` / `docker-compose.yml` locally, a ConfigMap in Kubernetes.
  - Secrets come from Kubernetes Secrets (later an external secrets manager), never from git.
- Spring profiles: `local`, `test`, `k8s`.

## Consequences

- One mechanism for discovery and config, identical across environments. That means two fewer services to run.
- Config changes are applied by rolling pods, not by refreshing them at runtime. That's acceptable, and safer.
- If we ever leave Kubernetes for bare VMs, revisit this decision.
- Spring Cloud remains in use for Gateway and OpenFeign.
