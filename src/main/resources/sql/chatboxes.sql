-- auto-generated definition
create table chatboxes
(
    id      bigint auto_increment
        primary key,
    userOne varchar(20) not null,
    userTwo varchar(20) not null
);

create index chatboxes_userOne_userTwo_index
    on chatboxes (userOne, userTwo);