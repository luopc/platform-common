package com.luopc.platform.common.core.resilience;

import lombok.Data;

@Data
public class HealthCheckResult {
    private String checkItem; // 检查项（如 database、redis）
    private boolean success;  // 是否成功
    private long costTime;    // 耗时（毫秒）
    private String errorMsg;  // 错误信息（失败时）
}
