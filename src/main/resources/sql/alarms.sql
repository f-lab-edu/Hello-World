create table alarms
(
    id        bigint auto_increment
        primary key,
    alarmTo   varchar(20)                               not null,
    alarmFrom varchar(20)                               not null,
    type      tinyint                                   not null,
    hasRead   enum ('Y', 'N') default 'N'               not null,
    createdAt timestamp       default CURRENT_TIMESTAMP not null
);