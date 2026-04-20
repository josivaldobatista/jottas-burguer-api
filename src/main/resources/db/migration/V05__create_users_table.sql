-- V5__create_users_table.sql

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(30)  NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_users_email ON users (email);
CREATE INDEX idx_users_active ON users (active);