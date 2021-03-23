create table languages
(
    id   smallint auto_increment
        primary key,
    name varchar(20) not null
);

create index languages_name_index
    on languages (name);