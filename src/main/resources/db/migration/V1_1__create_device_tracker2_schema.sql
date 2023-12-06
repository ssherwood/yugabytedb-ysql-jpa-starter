--
-- Adding second device tracker table variant for testing
--

CREATE TABLE IF NOT EXISTS yb_device_tracker2
(
    device_id    uuid        NOT NULL,
    media_id     text        NOT NULL,
    account_id   uuid        NOT NULL,
    status       varchar(16) NOT NULL,
    created_date timestamptz NOT NULL default current_timestamp,
    updated_date timestamptz NOT NULL default current_timestamp,
    CONSTRAINT yb_device_tracker2_pkey PRIMARY KEY (device_id HASH, media_id ASC)
) SPLIT INTO 30 TABLETS;

--
-- Using nonconcurrently as this table was just created
--
CREATE INDEX NONCONCURRENTLY IF NOT EXISTS yb_device_tracker2_account_id_idx ON yb_device_tracker2(account_id) split into 30 tablets;

--
-- NOTE: no additional index on updated_date; cleanup job will rely on yb_hash_code to split up job
--

--
-- load in some sample data that can be used for testing
--
INSERT INTO yb_device_tracker2 (device_id, media_id, account_id, status, updated_date)
SELECT uuid('cdd7cacd-8e0a-4372-8ceb-' || lpad(seq::text, 12, '0')),
       '48d1c2c2-0d83-43d9-' || lpad(seq2::text, 4, '0') || '-' || lpad(seq::text, 12, '0'),
       uuid('ff710c59-1e6d-47f9-a775-' || lpad(seq::text, 12, '0')),
       'ACTIVE' || seq,
       clock_timestamp()
FROM generate_series(0, 5000) AS seq,
     generate_series(0, 6) seq2;