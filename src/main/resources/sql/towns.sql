create table towns
(
    id        smallint auto_increment
        primary key,
    countryId smallint    not null,
    name      varchar(20) not null
);

create index towns_countryId_name_index
    on towns (countryId, name);