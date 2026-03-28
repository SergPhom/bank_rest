--liquibase formatted sql

--changeset serg_phom:alter-table-bank_user-add-column-is_active
alter table bank_user add column if not exists is_active boolean default true;
--rollback alter table bank_user drop column if exists is_active;