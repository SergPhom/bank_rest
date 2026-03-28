--liquibase formatted sql

--changeset serg_phom:create-table-card
create table if not exists card (
    id uuid primary key,
    number bigint,
    user_id uuid,
    valid_through date,
    status varchar(64) default 'CREATED',
    balance numeric(19, 2),

    constraint fk_card_user foreign key (user_id) references bank_user(id)
);
--rollback drop table if exists card;