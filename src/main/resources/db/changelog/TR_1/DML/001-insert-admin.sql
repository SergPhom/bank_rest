--liquibase formatted sql

--changeset serg_phom:insert-admin-with-role
with new_admin as (
    insert into bank_user (id, name, email, password)
    values (gen_random_uuid(), 'admin', 'admin@mail.com', '$2a$10$4GWba/tIXofprS47BZpg4.3bKvK.BfdIWq9Jo8dpmQ281ivUbstT6')
    returning id
)
insert into user_roles (user_id, role)
select id, 'ROLE_ADMIN' from new_admin;
--rollback delete from user_roles where user_id = (select id from bank_user where name = 'admin');
--rollback delete from bank_user where name = 'admin';