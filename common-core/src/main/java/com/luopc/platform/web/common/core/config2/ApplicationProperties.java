package com.luopc.platform.web.common.core.config2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//@Component
//@ConfigurationProperties(prefix = "app")
@Setter
@Getter
public class ApplicationProperties {

    private int connectionTimeout;
    private int readTimeout;
    private int maxConnections;
    private Map<String, String> features = new HashMap<>();

    // 初始化客户端的方法
    public HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectionTimeout))
                .build();
    }
}
