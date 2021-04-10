package me.soo.helloworld.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JasyptKeys {

    public static final String JASYPT_PASSWORD = System.getProperty("jasypt.encryptor.password");

    public static final String JASYPT_ALGORITHM = "PBEWithMD5AndDES";

    public static final String JASYPT_KEY_ITERATION = "1000";

    public static final String JASYPT_POOL_SIZE = "1";

    public static final String JASYPT_PROVIDER = "SunJCE";

    public static final String JASYPT_SALT_GENERATOR = "org.jasypt.salt.RandomSaltGenerator";

    public static final String JASYPT_IV_GENERATOR = "org.jasypt.iv.NoIvGenerator";

    public static final String JASYPT_STRING_OUTPUT_TYPE = "base64";
}
