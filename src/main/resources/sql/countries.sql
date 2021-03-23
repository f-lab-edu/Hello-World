create table countries
(
    id   smallint auto_increment
        primary key,
    name varchar(20) not null
);

create index countries_name_index
    on countries (name);