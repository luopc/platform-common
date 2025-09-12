package com.luopc.platform.common.core.exception;

/**
 * Error Code Interface
 *
 * @author Platform Team
 * @since 1.0.0
 */
public interface ErrorCode {

    /**
     * Get error code
     *
     * @return error code
     */
    int getCode();

    /**
     * Get error message
     *
     * @return error message
     */
    String getMessage();
}
