create table recommendations
(
    id        bigint auto_increment
        primary key,
    recomTo   varchar(20)                         not null,
    recomFrom varchar(20)                         not null,
    content   varchar(255)                        not null,
    writtenAt timestamp default CURRENT_TIMESTAMP not null
);

create index recommendations_recomTo_recomFrom_index
    on recommendations (recomTo, recomFrom);