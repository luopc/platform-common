package com.luopc.platform.common.core.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Trace Context Utility
 *
 * @author Platform Team
 * @since 1.0.0
 */
public class TraceContextUtil {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String USER_ID_KEY = "userId";
    private static final String TENANT_ID_KEY = "tenantId";
    private static final String REQUEST_ID_KEY = "requestId";

    /**
     * Generate trace ID
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate request ID
     */
    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Set trace ID
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * Get trace ID
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * Set user ID
     */
    public static void setUserId(String userId) {
        MDC.put(USER_ID_KEY, userId);
    }

    /**
     * Get user ID
     */
    public static String getUserId() {
        return MDC.get(USER_ID_KEY);
    }

    /**
     * Set tenant ID
     */
    public static void setTenantId(String tenantId) {
        MDC.put(TENANT_ID_KEY, tenantId);
    }

    /**
     * Get tenant ID
     */
    public static String getTenantId() {
        return MDC.get(TENANT_ID_KEY);
    }

    /**
     * Set request ID
     */
    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID_KEY, requestId);
    }

    /**
     * Get request ID
     */
    public static String getRequestId() {
        return MDC.get(REQUEST_ID_KEY);
    }

    /**
     * Clear all trace context
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * Clear specific key
     */
    public static void clear(String key) {
        MDC.remove(key);
    }

    /**
     * Initialize trace context
     */
    public static void initTrace() {
        setTraceId(generateTraceId());
        setRequestId(generateRequestId());
    }

    /**
     * Initialize trace context with trace ID
     */
    public static void initTrace(String traceId) {
        setTraceId(traceId);
        setRequestId(generateRequestId());
    }

    private TraceContextUtil() {
        // Utility class
    }
}
