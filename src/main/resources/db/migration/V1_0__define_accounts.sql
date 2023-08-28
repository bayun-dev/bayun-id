create table if not exists accounts (

    -- account
    id uuid not null,
    username varchar(32) not null unique,

    -- person
    first_name varchar(32),
    last_name varchar(32),
    date_of_birth varchar(10),
    gender varchar(6),

    -- secret
    hash varchar(60),
    last_modified_date bigint,

    -- contact
    email varchar(255),
    email_confirmed boolean,

    -- details
    registration_date bigint not null,

    -- deactivation
    deactivated boolean not null,
    deactivation_reason varchar(32),
    deactivation_reason_message varchar(255),
    deactivation_date bigint,

    primary key (id)
);

create unique index on accounts(username);

create table if not exists account_authorities (
    account_id uuid references accounts(id) on delete cascade,
    authority varchar(16) not null
);