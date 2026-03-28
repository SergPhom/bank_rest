--liquibase formatted sql

--changeset serg_phom:create-table-transfer
create table if not exists transfer (
    id uuid primary key,
    source_card_id uuid,
    target_card_id uuid,
    transfer_amount numeric(19, 2),
    result varchar(64),
    rejection_reason varchar(255),
    create_at timestamp
);
--rollback drop table if exists transfer;