ALTER TABLE ratings
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

UPDATE ratings
SET created_at = now();

ALTER TABLE ratings
    ALTER COLUMN created_at SET NOT NULL;