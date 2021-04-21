create table tokens
(
    id     bigint auto_increment
        primary key,
    userId varchar(20)  not null,
    token  varchar(255) null,
    constraint userId
        unique (userId)
);