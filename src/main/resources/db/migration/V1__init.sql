CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    created_at BIGINT,
    created_by BIGINT,
    updated_at BIGINT,
    updated_by BIGINT,
    deleted_at BIGINT,
    deleted_by BIGINT,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE INDEX idx_category_uuid ON category USING hash (uuid);

CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    created_at BIGINT,
    created_by BIGINT,
    updated_at BIGINT,
    updated_by BIGINT,
    deleted_at BIGINT,
    deleted_by BIGINT,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_tag_uuid ON tag USING hash (uuid);

CREATE TABLE post (
    id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    created_at BIGINT,
    created_by BIGINT,
    updated_at BIGINT,
    updated_by BIGINT,
    deleted_at BIGINT,
    deleted_by BIGINT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE,
    content TEXT NOT NULL,
    excerpt TEXT,
    featured_image VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    published_at BIGINT,
    category_id BIGINT NOT NULL REFERENCES category(id)
);

CREATE INDEX idx_post_uuid ON post USING hash (uuid);
CREATE INDEX idx_post_content_fts ON post USING gin (to_tsvector('english', content));

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL REFERENCES post(id),
    tag_id BIGINT NOT NULL REFERENCES tag(id),
    PRIMARY KEY (post_id, tag_id)
);
