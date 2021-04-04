package me.soo.helloworld.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSourceTypes {

    public static final String MASTER_DB = "master";

    public static final String SLAVE_DB = "slave";
}
