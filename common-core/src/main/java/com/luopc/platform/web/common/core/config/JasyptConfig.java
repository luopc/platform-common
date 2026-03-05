package com.luopc.platform.web.common.core.config;

import com.luopc.platform.web.common.core.util.JasyptEncryptorUtil;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(
            @Value("${jasypt.encryptor.password}") String secretKey,
            @Value("${jasypt.encryptor.algorithm}") String algorithm) {
        log.info("Jasypt Encryptor initialized with algorithm: {}", algorithm);
        return JasyptEncryptorUtil.getPooledPBEStringEncryptor(secretKey, algorithm);
    }


}
