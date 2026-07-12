CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    created_at BIGINT,
    created_by BIGINT,
    updated_at BIGINT,
    updated_by BIGINT,
    deleted_at BIGINT,
    deleted_by BIGINT,
    username VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_ADMIN'
);

CREATE INDEX idx_users_uuid ON users USING hash (uuid);
CREATE INDEX idx_users_username ON users (username);
