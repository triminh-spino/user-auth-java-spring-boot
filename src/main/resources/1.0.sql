DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id uuid NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(255),
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NULL,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tokens (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type SMALLINT,
    revoked BOOLEAN,
    expired DATE,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NULL,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
    PRIMARY KEY (id)
);