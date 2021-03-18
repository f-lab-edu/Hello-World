create table block
(
    id      bigint auto_increment
        primary key,
    userId  varchar(20) not null,
    blockId varchar(20) not null
);

create index table_name_userId_blockId_index
    on block (userId, blockId);