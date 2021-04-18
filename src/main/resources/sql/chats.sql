-- auto-generated definition
create table chats
(
    id        bigint auto_increment
        primary key,
    chatBoxId bigint                                    not null,
    recipient varchar(20)                               not null,
    content   varchar(500)                              not null,
    hasRead   enum ('Y', 'N') default 'N'               not null,
    sentAt    timestamp       default CURRENT_TIMESTAMP not null,
    sender    varchar(20)                               not null
);

create index chats_to_from_index
    on chats (chatBoxId, sender, recipient);

