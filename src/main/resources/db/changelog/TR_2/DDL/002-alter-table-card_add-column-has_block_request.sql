--liquibase formatted sql

--changeset serg_phom:alter-table-card-add-column-has_block_request
alter table card add column if not exists has_block_request boolean default false;
--rollback alter table card drop column if exists has_block_request;