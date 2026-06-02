-- ─────────────────────────────────────────────────────────────────────────────
-- Notebookum — persistence_service
-- Script de inicialización PostgreSQL
-- Ejecutar una sola vez antes del primer arranque con ddl-auto: validate
-- ─────────────────────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL       PRIMARY KEY,
    email      VARCHAR(120)    NOT NULL UNIQUE,
    name       VARCHAR(100)    NOT NULL,
    created_at TIMESTAMP       NOT NULL
);

CREATE TABLE IF NOT EXISTS notebooks (
    id          BIGSERIAL       PRIMARY KEY,
    user_id     BIGINT          NOT NULL REFERENCES users(id),
    name        VARCHAR(255)    NOT NULL,
    description TEXT,
    created_at  TIMESTAMP       NOT NULL
);

CREATE TABLE IF NOT EXISTS conversations (
    id          BIGSERIAL       PRIMARY KEY,
    notebook_id BIGINT          NOT NULL REFERENCES notebooks(id),
    title       VARCHAR(255),
    created_at  TIMESTAMP       NOT NULL
);

CREATE TABLE IF NOT EXISTS messages (
    id              BIGSERIAL   PRIMARY KEY,
    conversation_id BIGINT      NOT NULL REFERENCES conversations(id),
    role            VARCHAR(50) NOT NULL,
    content         TEXT        NOT NULL,
    created_at      TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS documents (
    id             SERIAL       PRIMARY KEY,
    user_id        BIGINT       NOT NULL REFERENCES users(id),
    notebook_id    BIGINT       REFERENCES notebooks(id),
    filename       VARCHAR(255) NOT NULL,
    file_path      VARCHAR(512) NOT NULL,
    job_id         VARCHAR(36),
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    extracted_text TEXT,
    created_at     TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS summaries (
    id           SERIAL      PRIMARY KEY,
    document_id  INTEGER     NOT NULL REFERENCES documents(id),
    content      TEXT        NOT NULL,
    model_used   VARCHAR(100) NOT NULL DEFAULT 'gpt-4o',
    created_at   TIMESTAMP   NOT NULL
);