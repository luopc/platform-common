package com.luopc.platform.web.common.core.config;

import jakarta.el.PropertyNotFoundException;

import java.util.Properties;

public class PropertyLoader {
    // 负责加解密的服务，它会从SecretKeyService取得密钥并对给定字符串完成加解密
    private final EncryptionDecryptionService encryptionDecryptionService;
    // 配置中心的客户端
    private final ConfigurationCenterClient configurationCenterClient;
    // 配置文件
    private final Properties properties;

    public PropertyLoader(
            final EncryptionDecryptionService encryptionDecryptionService,
            final ConfigurationCenterClient configurationCenterClient,
            final Properties properties) {
        this.encryptionDecryptionService = encryptionDecryptionService;
        this.configurationCenterClient = configurationCenterClient;
        this.properties = properties;
    }

    /**
     * 从配置中心或配置文件取得一个key的值
     *
     * @param key Property或配置中心条目的key
     * @return 取到的值，不会为null
     * @throws PropertyNotFoundException 当在配置中心和配置文件中都找不到这个key时抛出，这通常意味着我们把某条配置漏掉了
     */
    public String loadProperty(final String key) {
        // 首先尝试从配置中心取值，如果取不到则返回null
        String value = configurationCenterClient.getValue(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        if (value == null) {
            throw new PropertyNotFoundException(key);
        }

        return value;
    }

    /**
     * 从配置中心或配置文件取得一个key的值并将其解密
     *
     * @param key Property或配置中心条目的key
     * @return 取到的值，不会为null
     * @throws PropertyNotFoundException 当在配置中心和配置文件中都找不到这个key时抛出，这通常意味着我们把某条配置漏掉了
     */
    public String loadEncryptedProperty(final String key) {
        final String encryptedValue = configurationCenterClient.getValue(key);
        if (encryptedValue == null) {
            // 如果从配置中心拿不到值，那就从property文件中拿未加密的原文
            final String plainValue = properties.getProperty(key);
            if (plainValue == null) {
                throw new PropertyNotFoundException(key);
            }

            return plainValue;
        }

        return encryptionDecryptionService.decrypt(encryptedValue);
    }

    /**
     * 检查配置文件或配置中心是否有指定的key
     *
     * @param key 要检查的key
     * @return 当这个key存在时返回true，反之返回false
     */
    public boolean hasProperty(final String key) {
        return configurationCenterClient.getKeys().contains(key) || properties.containsKey(key);
    }
}
