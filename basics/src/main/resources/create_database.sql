drop database bootifulkotlin;
create database bootifulkotlin;

create table bootifulkotlin.customer
(
    id   int auto_increment,
    name varchar(255) null,
    constraint customers_pk
        primary key (id)
);

insert into bootifulkotlin.customer(name)
values ('Bram'),
       ('Mieke'),
       ('Alice'),
       ('Harry');
