-- read_access
create role read_access;

grant connect on database "user"
 to read_access;

grant usage on schema public to read_access;

grant select on all tables in schema public to read_access;
grant select on all sequences in schema public to read_access;
alter default privileges in schema public grant select on tables to read_access;
alter default privileges in schema public grant select on sequences to read_access;

-- write_access
create role write_access;

grant connect on database "user"
 to write_access;

grant usage on schema public to write_access;

grant insert, update, delete, truncate on all tables in schema public to write_access;
alter default privileges in schema public grant insert, update, delete, truncate on tables to write_access;
