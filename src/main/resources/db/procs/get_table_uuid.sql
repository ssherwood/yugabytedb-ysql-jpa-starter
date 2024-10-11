--
-- helper function to return the UUID for a given table/schema
--
-- usage:
--   select get_table_id('foo'); # by table in default 'public' schema
--   select get_table_id('foo', 'public');
--
CREATE OR REPLACE FUNCTION get_table_uuid(table_name_p TEXT, schema_name_p TEXT DEFAULT 'public') RETURNS TEXT
AS
$$
SELECT '0000' || lpad(to_hex(d.oid::int), 4, '0') || '00003000800000000000' || lpad(to_hex(c.oid::int), 4, '0') tableid
FROM pg_class c, pg_namespace n, pg_database d
WHERE n.nspname = get_table_id.schema_name_p
  AND c.relname = get_table_id.table_name_p
  AND d.datname = current_database();
$$ LANGUAGE SQL;
