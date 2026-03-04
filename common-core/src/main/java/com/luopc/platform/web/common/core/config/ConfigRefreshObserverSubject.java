package com.luopc.platform.web.common.core.config;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

public abstract class ConfigRefreshObserverSubject {
    // 观察到配置更新后，从PropertyLoader获取新的值
    @Getter
    private final PropertyLoader propertyLoader;
    // 这个观察者关心的key
    // 因为要计算在这个对象中当前生效的配置的签名，所以要配置这个对象关心哪些key
    // 然后取出这些key对应的value来计算签名
    private final List<String> monitoredKeys;
    // 当前生效的配置的一个签名
    // 可以将其理解为当前配置的一个哈希，实际上是综合当前生效的配置生成的一个UUID
    // 在收到配置刷新事件后，会将新配置的签名与当前签名比较
    // 仅在签名不一致时刷新对象中的配置
    @Getter
    private String configSignature;

    public ConfigRefreshObserverSubject(
            final PropertyLoader propertyLoader,
            final List<String> monitoredKeys
    ) {
        this.propertyLoader = propertyLoader;
        this.monitoredKeys = monitoredKeys;
        // 在初始化时计算当前生效配置的签名
        this.configSignature = calculateConfigurationSignature();
    }

    /**
     * 这个方法留给实际成为subject的类去实现具体它要怎么刷新自己的配置
     */
    public abstract void refreshConfigImpl();

    /**
     * 这个方法留给notifier调用，实现更新配置及刷新配置签名
     */
    public final void refreshConfig() {
        refreshConfigImpl();
        configSignature = calculateConfigurationSignature();
    }

    protected String calculateConfigurationSignature() {
        final StringBuilder newConfigurationSignatureSeedBuilder = new StringBuilder();
        for (String key : monitoredKeys) {
            if (!propertyLoader.hasProperty(key)) {
                // 如果找不到某个关心的key，那么说明要么初始配置有问题，要么刷新的配置有问题
                // 这时候尽早抛出异常引发开发人员关注
                throw new IllegalArgumentException("Missing property: " + key + " for class: " + this.getClass().getSimpleName());
            }

            newConfigurationSignatureSeedBuilder.append(propertyLoader.loadProperty(key));
        }

        return UUID.nameUUIDFromBytes(newConfigurationSignatureSeedBuilder.toString().getBytes()).toString();
    }
}
