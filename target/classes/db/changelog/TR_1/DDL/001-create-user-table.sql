--liquibase formatted sql

--changeset serg_phom:create-table-bank_user
create table if not exists bank_user (
    id uuid primary key,
    name varchar(64),
    email varchar(64),
    password varchar(64)
);
--rollback drop table if exists bank_user;

--changeset serg_phom:create-table-user_roles
create table if not exists user_roles (
    user_id uuid not null,
    role varchar(20),
    constraint fk_user_roles_user
        foreign key (user_id) references bank_user (id)
        on delete cascade
);
--rollback drop table if exists user_roles;