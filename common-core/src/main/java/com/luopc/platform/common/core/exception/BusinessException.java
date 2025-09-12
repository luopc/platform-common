package com.luopc.platform.common.core.exception;

import lombok.Getter;

/**
 * Business Exception
 *
 * @author Platform Team
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;
    private final Object data;

    public BusinessException(String message) {
        this(500, message, null);
    }

    public BusinessException(int code, String message) {
        this(code, message, null);
    }

    public BusinessException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public BusinessException(ErrorCode errorCode, Object data) {
        this(errorCode.getCode(), errorCode.getMessage(), data);
    }
}
