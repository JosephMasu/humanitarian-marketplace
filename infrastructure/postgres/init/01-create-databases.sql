-- Runs once, on first start of an empty volume. One database and owner per service:
-- no service may read another's database (see CLAUDE.md). Dev-only passwords.
CREATE USER keycloak WITH PASSWORD 'keycloak';
CREATE DATABASE keycloak OWNER keycloak;

CREATE USER user_service WITH PASSWORD 'user_service';
CREATE DATABASE user_service OWNER user_service;

CREATE USER organization_service WITH PASSWORD 'organization_service';
CREATE DATABASE organization_service OWNER organization_service;
