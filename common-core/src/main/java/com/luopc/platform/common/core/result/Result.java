package com.luopc.platform.common.core.result;

import com.luopc.platform.common.core.exception.ErrorCode;
import com.luopc.platform.common.core.exception.PlatformErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Common Result
 *
 * @author Platform Team
 * @since 1.0.0
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Response code
     */
    private int code;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data
     */
    private T data;

    /**
     * Response timestamp
     */
    private LocalDateTime timestamp;

    /**
     * Trace ID
     */
    private String traceId;

    public Result() {
        this.timestamp = LocalDateTime.now();
    }

    public Result(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ErrorCode errorCode, T data) {
        this(errorCode.getCode(), errorCode.getMessage(), data);
    }

    /**
     * Success result without data
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * Success result with data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(PlatformErrorCode.SUCCESS, data);
    }

    /**
     * Success result with custom message
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(PlatformErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * Error result with message
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(PlatformErrorCode.INTERNAL_SERVER_ERROR.getCode(), message, null);
    }

    /**
     * Error result with code and message
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * Error result with error code
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode, null);
    }

    /**
     * Error result with error code and data
     */
    public static <T> Result<T> error(ErrorCode errorCode, T data) {
        return new Result<>(errorCode, data);
    }

    /**
     * Check if result is successful
     */
    public boolean isSuccess() {
        return this.code == PlatformErrorCode.SUCCESS.getCode();
    }

    /**
     * Set trace ID
     */
    public Result<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
