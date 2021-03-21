create table friends
(
    id       int auto_increment
        primary key,
    userId   varchar(20) not null,
    friendId varchar(20) not null,
    status   tinyint     not null,
    friendedAt timestamp   null
);

create index friends_userId_friendId_index
    on friends (userId, friendId);