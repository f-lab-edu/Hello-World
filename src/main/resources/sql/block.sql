create table block
(
    id        bigint auto_increment
        primary key,
    blockId   varchar(20) charset latin1 not null,
    blockedBy varchar(20) charset latin1 not null
);

create index block_userId_blockId_index
    on block (blockId, blockedBy);
