--
-- Used for JSON as Map sample
--

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS json_as_map (
    id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    alternate_id     VARCHAR(10) NOT NULL,
    points           INT         NOT NULL CHECK(points >= 0),
    metadata         JSONB       NOT NULL,
    created_date     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
) SPLIT INTO 12 TABLETS;

CREATE UNIQUE INDEX NONCONCURRENTLY
    IF NOT EXISTS json_as_map_alternate_id_uq_idx
    ON json_as_map (alternate_id) SPLIT INTO 12 TABLETS;
