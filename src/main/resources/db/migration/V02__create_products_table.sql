-- V02__create_products_table.sql

CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150)   NOT NULL,
    description VARCHAR(500)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    category_id BIGINT         NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_products_active ON products (active);