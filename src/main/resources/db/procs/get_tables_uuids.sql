--
-- helper function to return the UUIDs for the current user/database
--
-- usage:
--
CREATE OR REPLACE FUNCTION get_tables_uuids(schema_name_p TEXT DEFAULT 'public')
  RETURNS TABLE (owner NAME, schema NAME, table_name NAME, table_type TEXT, table_uuid TEXT)
AS $$
SELECT pg_roles.rolname AS owner,
       pg_namespace.nspname AS schema,
       pg_class.relname AS table_name,
       CASE pg_class.relkind
           WHEN 'r' THEN 'table'
           WHEN 'i' THEN 'index'
           WHEN 'S' THEN 'sequence'
           WHEN 'v' THEN 'view'
           WHEN 'm' THEN 'materialized view'
           WHEN 'c' THEN 'composite type'
           WHEN 't' THEN 'toast table'
           WHEN 'f' THEN 'foreign table'
           ELSE 'other'
       END AS table_type,
       '0000' || lpad(to_hex(pg_database.oid::int), 4, '0') || '00003000800000000000' || lpad(to_hex(pg_class.oid::int), 4, '0') as table_uuid
  FROM pg_class
  JOIN pg_roles ON pg_roles.oid = pg_class.relowner
  JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
  JOIN pg_database ON pg_database.datname = current_database()
 WHERE pg_roles.rolname = current_user
   AND pg_namespace.nspname NOT IN ('pg_catalog', 'information_schema')
 ORDER BY schema, table_name, table_type
$$ LANGUAGE SQL;
