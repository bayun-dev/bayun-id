create table email_update_tokens (
    id uuid not null,
    account_id uuid unique not null,
    email varchar(320) not null,
    date bigint not null,
    primary key (id)
);