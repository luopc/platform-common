package com.luopc.platform.common.core.constant;

/**
 * System Constants
 *
 * @author Platform Team
 * @since 1.0.0
 */
public class SystemConstants {

    /**
     * Default encoding
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Default time zone
     */
    public static final String DEFAULT_TIMEZONE = "Asia/Shanghai";

    /**
     * Default date format
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Default date format without time
     */
    public static final String DEFAULT_DATE_FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * Success code
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * Error code
     */
    public static final int ERROR_CODE = 500;

    /**
     * Unauthorized code
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * Forbidden code
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * Not found code
     */
    public static final int NOT_FOUND_CODE = 404;

    /**
     * Default page size
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Maximum page size
     */
    public static final int MAX_PAGE_SIZE = 1000;

    /**
     * Trace ID header
     */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * User ID header
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * Tenant ID header
     */
    public static final String TENANT_ID_HEADER = "X-Tenant-Id";

    /**
     * Request ID header
     */
    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    private SystemConstants() {
        // Utility class
    }
}
