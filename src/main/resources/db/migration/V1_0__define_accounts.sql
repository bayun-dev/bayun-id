create table if not exists account_emails (
    id uuid not null,
    email varchar(384) not null,
    confirmed boolean not null,
    primary key (id)
);

create index on account_emails(email);

create table if not exists accounts (
    id uuid not null,
    username varchar(60) not null unique,
    first_name varchar(60),
    last_name varchar(60),
    password_hash varchar(60),
    avatar_id varchar(60),
    email_id uuid references account_emails(id),
    blocked boolean not null,
    deleted boolean not null,
    primary key (id)
);

create unique index on accounts(username);
create unique index on accounts(email_id);

create table if not exists account_authorities (
    account_id uuid references accounts(id) on delete cascade,
    authority varchar(16) not null
);