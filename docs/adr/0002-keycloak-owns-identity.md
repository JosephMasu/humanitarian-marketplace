# ADR-0002: Keycloak owns identity; user-service owns profiles only

- Status: Accepted
- Date: 2026-11-24

## Context

The initial layout had an `identity-service` alongside Keycloak. If both handle users, we rebuild registration, credentials and roles twice, and the two copies drift apart.

## Decision

**Keycloak is the single source of truth for:**
- Registration
- Login, passwords, MFA, social/IdP login
- Sessions and tokens
- Platform-wide roles, as realm roles: `platform-admin`, `ngo-staff`, `supplier`, `donor`. Adjust these once the domain is grilled.

**`identity-service` is renamed `user-service`.** It owns only marketplace data about a person:
- Display profile, contact preferences, language, notification settings
- Keyed by the Keycloak subject (`sub` claim, a UUID). No passwords, no role tables.
- Created just in time: on the first authenticated request, a profile row is created if it doesn't exist yet.
- Admin actions that must change Keycloak (inviting a user, disabling an account) go through the Keycloak Admin REST API, using a service-account client. Only user-service calls that API.

**Organization membership** (which org a user belongs to, and their role inside it) lives in `organization-service`, keyed by Keycloak `sub`. It isn't stored as Keycloak groups. That keeps the business rules about orgs in our code.

**Authentication:**
- The gateway and every service validate JWTs as OAuth2 resource servers (Spring Security), using Keycloak's JWKS endpoint.
- Services never trust headers the gateway adds in place of the token.

## Consequences

- No password or credential code in our services.
- Two lookups to get the full picture of a user: roles from the token, profile and memberships from our services. That's acceptable.
- Keycloak realm config is versioned in `infrastructure/keycloak/` and imported on startup.
