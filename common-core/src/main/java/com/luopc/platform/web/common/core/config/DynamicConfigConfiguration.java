package com.luopc.platform.web.common.core.config;

import com.luopc.platform.web.common.core.service.ConfigurationRefreshService;
import com.luopc.platform.web.common.core.service.DynamicConfigRefreshService;
import com.luopc.platform.web.common.core.service.EncryptionDecryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicConfigConfiguration {

    @Value("${config.server.url:http://localhost:8082}")
    private String configServerUrl;

    @Bean
    public ConfigurationRefreshService configurationRefreshClient() {
        return new ConfigurationRefreshService(configServerUrl);
    }

    @Bean
    public DynamicConfigRefreshService dynamicConfigRefreshService(
            ConfigurationRefreshService configurationRefreshService,
            EncryptionDecryptionService encryptionDecryptionService) {
        return new DynamicConfigRefreshService(configurationRefreshService, encryptionDecryptionService);
    }
}
