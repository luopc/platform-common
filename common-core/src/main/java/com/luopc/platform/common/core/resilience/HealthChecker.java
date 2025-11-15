package com.luopc.platform.common.core.resilience;

public interface HealthChecker {

    String getCheckItem(); // 获取检查项名称
    HealthCheckResult check(); // 执行检查
}
