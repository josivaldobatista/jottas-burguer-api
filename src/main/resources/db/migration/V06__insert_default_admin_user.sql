-- V06__insert_default_admin_user.sql

INSERT INTO users (name, email, password, role, active, created_at, updated_at)
VALUES ('Admin',
        'admin@jottasburger.com',
        '$2a$10$wOh74mSOE6L05zS.XLXF8.bk.Q9J96SoeKzZKgHsFUXw.lgooePEq',
        'ADMIN',
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);