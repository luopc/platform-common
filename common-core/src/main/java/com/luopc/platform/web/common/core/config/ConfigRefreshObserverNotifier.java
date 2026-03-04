package com.luopc.platform.web.common.core.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigRefreshObserverNotifier {

    // 用于存放各个subject的注册表
    // 在某些情况下，同一个subject可能会被重复注册，所以这里我会把subject的类名作为key放在map中
    // 方便在注册时检查是否重复注册
    private static final Map<String, ConfigRefreshObserverSubject> subjects = new HashMap<>();

    private ConfigRefreshObserverNotifier() {
        // notifier作为一个单例，我们不希望它被实例化
        throw new UnsupportedOperationException("ConfigRefreshObserverNotifier shouldn't be instantiated");
    }

    /**
     * 注册subject
     *
     * @param subject 待注册的subject对象
     */
    public static void register(final ConfigRefreshObserverSubject subject) {
        if (!isSubjectRegistered(subject)) {
            subjects.put(subject.getClass().getSimpleName(), subject);
        }
    }

    /**
     * 向各个subject发出配置更新的通知
     */
    public static void notifyObservers() {
        for (final ConfigRefreshObserverSubject subject : subjects.values()) {
            // 计算新配置的签名
            final String newConfigSignature = subject.calculateConfigurationSignature();
            // 仅当签名不同，即配置有变化时，才通知对应的subject更新
            if (!newConfigSignature.equals(subject.getConfigSignature())) {
                subject.refreshConfig();
            }
        }
    }

    private static boolean isSubjectRegistered(final ConfigRefreshObserverSubject subject) {
        final String subjectClassName = subject.getClass().getSimpleName();
        return subjects.containsKey(subjectClassName);
    }

}
