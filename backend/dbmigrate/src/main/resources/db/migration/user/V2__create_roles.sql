-- read_access
CREATE ROLE read_access;

GRANT CONNECT ON DATABASE "user"
 TO read_access;

GRANT USAGE ON SCHEMA public TO read_access;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO read_access;

-- write_access
CREATE ROLE write_access;

GRANT CONNECT ON DATABASE "user"
 TO write_access;

GRANT USAGE ON SCHEMA public TO write_access;

GRANT INSERT, UPDATE, DELETE, TRUNCATE ON ALL TABLES IN SCHEMA public TO write_access;
