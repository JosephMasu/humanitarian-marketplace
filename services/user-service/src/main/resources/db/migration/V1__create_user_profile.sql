-- Marketplace profile for a person. Identity, credentials and roles live in Keycloak (ADR-0002).
-- The primary key is the Keycloak subject (`sub` claim).
CREATE TABLE user_profile (
    id            UUID PRIMARY KEY,
    display_name  VARCHAR(200),
    email         VARCHAR(320),
    locale        VARCHAR(35),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
