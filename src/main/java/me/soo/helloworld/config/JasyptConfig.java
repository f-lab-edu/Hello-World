package me.soo.helloworld.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static me.soo.helloworld.util.constant.JasyptKeys.*;

@Configuration
@Profile("prod")
public class JasyptConfig {

    @Bean
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(JASYPT_PASSWORD);
        config.setAlgorithm(JASYPT_ALGORITHM);
        config.setKeyObtentionIterations(JASYPT_KEY_ITERATION);
        config.setPoolSize(JASYPT_POOL_SIZE);
        config.setProviderName(JASYPT_PROVIDER);
        config.setSaltGeneratorClassName(JASYPT_SALT_GENERATOR);
        config.setIvGeneratorClassName(JASYPT_IV_GENERATOR);
        config.setStringOutputType(JASYPT_STRING_OUTPUT_TYPE);
        encryptor.setConfig(config);
        return encryptor;
    }



}
