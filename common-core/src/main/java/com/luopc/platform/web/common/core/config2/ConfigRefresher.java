package com.luopc.platform.web.common.core.config2;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Properties;

//@Component
@RequiredArgsConstructor
public class ConfigRefresher {

    private final ApplicationProperties properties;
    private final ApplicationContext applicationContext;
    @Getter
    private HttpClient httpClient;

    @PostConstruct
    public void init() {
        refreshHttpClient();
    }

//    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChange() {
        refreshHttpClient();
    }

    private void refreshHttpClient() {
        httpClient = properties.buildHttpClient();
        System.out.println("HttpClient refreshed with timeout: " + properties.getConnectionTimeout());
    }

    // 手动触发配置刷新的方法
    public void refreshProperties(Map<String, Object> newProps) {
        PropertiesPropertySource propertySource = new PropertiesPropertySource(
                "dynamic", convertToProperties(newProps));

        ConfigurableEnvironment env = (ConfigurableEnvironment) applicationContext.getEnvironment();
        env.getPropertySources().addFirst(propertySource);

        // 触发环境变更事件
//        applicationContext.publishEvent(new EnvironmentChangeEvent(newProps.keySet()));
    }

    private Properties convertToProperties(Map<String, Object> map) {
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            properties.put(entry.getKey(), entry.getValue().toString());
        }
        return properties;
    }
}
