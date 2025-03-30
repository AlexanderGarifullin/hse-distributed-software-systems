-- liquibase formatted sql

-- changeset alexandergarifullin:1
ALTER TABLE dss.tests
  DROP CONSTRAINT fk_task,
  ADD CONSTRAINT fk_task
    FOREIGN KEY (task_id)
    REFERENCES dss.tasks(id)
    ON DELETE CASCADE;

