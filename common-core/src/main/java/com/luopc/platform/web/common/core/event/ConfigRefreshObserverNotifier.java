package com.luopc.platform.web.common.core.event;

import java.util.HashMap;
import java.util.Map;

public class ConfigRefreshObserverNotifier {
    /**
     * save configuration subject, key is subject class name
     */
    private static final Map<String, ConfigRefreshObserverSubject> subjects = new HashMap<>();

    private ConfigRefreshObserverNotifier() {
        throw new UnsupportedOperationException("ConfigRefreshObserverNotifier shouldn't be instantiated");
    }

    /**
     * register subject
     */
    public static void register(final ConfigRefreshObserverSubject subject) {
        if (!isSubjectRegistered(subject)) {
            subjects.put(subject.getClass().getSimpleName(), subject);
        }
    }

    /**
     * notify all registered subjects
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
