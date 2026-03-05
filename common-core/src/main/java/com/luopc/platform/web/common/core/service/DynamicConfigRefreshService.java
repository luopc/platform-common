package com.luopc.platform.web.common.core.service;

import com.luopc.platform.web.common.core.event.ConfigRefreshObserverNotifier;
import com.luopc.platform.web.common.core.model.ConfigItem;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicConfigRefreshService {

    private final ConfigurationRefreshService configurationRefreshService;
    private final EncryptionDecryptionService encryptionDecryptionService;

    @Value("${config.refresh.interval:30}")
    private int refreshIntervalSeconds;

    @Value("${config.server.url:http://localhost:8082}")
    private String configServerUrl;

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledTask;
    private Properties currentProperties = new Properties();
//
//    @PostConstruct
//    public void init() {
//        taskScheduler.initialize();
//        taskScheduler.start();
//
//        // 初始加载配置
//        loadInitialConfigs();
//
//        // 定时刷新配置
//        scheduledTask = taskScheduler.scheduleAtFixedRate(
//                this::refreshConfigs,
//                TimeUnit.SECONDS.toMillis(refreshIntervalSeconds)
//        );
//
//        log.info("Dynamic config refresh service started with interval: {} seconds", refreshIntervalSeconds);
//    }

    @PreDestroy
    public void destroy() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        taskScheduler.shutdown();
        log.info("Dynamic config refresh service stopped");
    }

    private void loadInitialConfigs() {
        try {
            List<ConfigItem> configs = configurationRefreshService.fetchAllConfigs();
            processAndCacheConfigs(configs);
        } catch (Exception e) {
            log.error("Failed to load initial configs", e);
        }
    }

    private void refreshConfigs() {
        try {
            List<ConfigItem> newConfigs = configurationRefreshService.fetchAllConfigs();

            if (hasConfigsChanged(newConfigs)) {
                log.info("Configuration changes detected, refreshing...");
                processAndCacheConfigs(newConfigs);

                // 通知所有观察者刷新配置
                ConfigRefreshObserverNotifier.notifyObservers();

                log.info("Configuration refresh completed");
            }
        } catch (Exception e) {
            log.error("Error during config refresh", e);
        }
    }

    private boolean hasConfigsChanged(List<ConfigItem> newConfigs) {
        if (newConfigs.size() != currentProperties.size()) {
            return true;
        }

        for (ConfigItem newItem : newConfigs) {
            String key = newItem.getKey();
            String newValue = newItem.getValue();

            if (!currentProperties.containsKey(key)) {
                return true;
            }

            String oldValue = currentProperties.getProperty(key);
            if (oldValue != null && !oldValue.equals(newValue)) {
                return true;
            }
        }

        return false;
    }

    private void processAndCacheConfigs(List<ConfigItem> configs) {
        Properties newProperties = new Properties();

        for (ConfigItem item : configs) {
            String value = item.getValue();

            // 如果需要解密，则解密
            if (Boolean.TRUE.equals(item.getIsEncrypted())) {
                try {
                    value = encryptionDecryptionService.decrypt(value);
                    log.debug("Decrypted config key: {}", item.getKey());
                } catch (Exception e) {
                    log.error("Failed to decrypt config key: {}", item.getKey(), e);
                    continue;
                }
            }

            newProperties.setProperty(item.getKey(), value);
        }

        // 更新缓存
        configurationRefreshService.updateCachedConfigs(configs);
        currentProperties = newProperties;
    }

}
