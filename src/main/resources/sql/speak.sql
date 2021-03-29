create table speak
(
    id     smallint auto_increment
        primary key,
    userId varchar(20)      not null,
    langId int              not null,
    level  tinyint unsigned not null,
    status tinyint unsigned not null
);

create index speak_userId_langId_index
    on speak (userId, langId);
