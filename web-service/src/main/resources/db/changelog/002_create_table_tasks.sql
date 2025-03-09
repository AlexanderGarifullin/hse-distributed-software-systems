-- liquibase formatted sql

-- changeset alexandergarifullin:1
CREATE TABLE IF NOT EXISTS dss.tasks (
    id BIGSERIAL PRIMARY KEY,
    time_limit INT NOT NULL CHECK (time_limit BETWEEN 250 AND 15000),
    memory_limit INT NOT NULL CHECK (memory_limit BETWEEN 4 AND 1024),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT,
    legend TEXT,
    input TEXT,
    output TEXT,
    generation_rules TEXT
);

-- changeset alexandergarifullin:2
CREATE INDEX idx_tasks_created_at
  ON dss.tasks (created_at);