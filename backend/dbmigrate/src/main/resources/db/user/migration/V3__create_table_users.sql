create table if not exists "users"
(
    id              uuid primary key   default gen_random_uuid(),
    email           varchar   not null unique,
    name            varchar,
    surname         varchar,
    password_hash   varchar,
    created_at      timestamp not null default now(),
    last_updated_at timestamp not null default now()
);