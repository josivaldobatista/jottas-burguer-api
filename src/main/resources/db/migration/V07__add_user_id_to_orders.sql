-- V07__add_user_id_to_orders.sql

ALTER TABLE orders
    ADD COLUMN user_id BIGINT;

UPDATE orders
SET user_id = (
    SELECT id
    FROM users
    WHERE email = 'admin@jottasburger.com'
    LIMIT 1
    );

ALTER TABLE orders
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users(id);

CREATE INDEX idx_orders_user_id ON orders(user_id);