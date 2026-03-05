package com.luopc.platform.web.common.core.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luopc.platform.web.common.core.model.ConfigItem;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * <a href="https://mp.weixin.qq.com/s/vzsTzWDPA6W149FXs5oEGQ">利用观察者模式实现动态刷新 Bean 中的配置</a>
 * <a href="https://mp.weixin.qq.com/s/W0o1_FkNg_uUxXCn7OekyA">SpringBoot 中 10 种动态修改配置的方法</a>
 */
@Slf4j
public class ConfigurationRefreshService {

    private final String configServerUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private Map<String, ConfigItem> cachedConfigs = new HashMap<>();

    public ConfigurationRefreshService(String configServerUrl) {
        this.configServerUrl = configServerUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<ConfigItem> fetchAllConfigs() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(configServerUrl + "/admin/config/getAll"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ConfigItem> configs = objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<ConfigItem>>() {
                        }
                );
                log.info("Fetched {} configs from config server", configs.size());
                return configs;
            } else {
                log.error("Failed to fetch configs, status code: {}", response.statusCode());
                return Collections.emptyList();
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse config response", e);
            return Collections.emptyList();
        } catch (InterruptedException e) {
            log.error("Config fetch interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch configs", e);
            return Collections.emptyList();
        }
    }

    public String getValue(String key) {
        ConfigItem item = cachedConfigs.get(key);
        return item != null ? item.getValue() : null;
    }

    public Set<String> getKeys() {
        return cachedConfigs.keySet();
    }

    public void updateCachedConfigs(List<ConfigItem> newConfigs) {
        cachedConfigs = new HashMap<>();
        for (ConfigItem item : newConfigs) {
            cachedConfigs.put(item.getKey(), item);
        }
        log.info("Config cache updated. Total: {} items", cachedConfigs.size());
    }

    public boolean hasConfigChanged() {
        return !cachedConfigs.isEmpty();
    }

    public boolean hasProperty(String key) {
        return cachedConfigs.containsKey(key);
    }
}
