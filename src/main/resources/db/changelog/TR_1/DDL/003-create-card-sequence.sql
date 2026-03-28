--liquibase formatted sql

--changeset serg_phom:create-card-sequence
create sequence card_number_seq start with 1 increment by 1;
--rollback drop sequence if exists card_number_seq;