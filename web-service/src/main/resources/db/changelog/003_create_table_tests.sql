-- liquibase formatted sql

-- changeset alexandergarifullin:1
CREATE TABLE IF NOT EXISTS dss.tests (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGSERIAL NOT NULL,
    input TEXT NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES dss.tasks(id)
);
