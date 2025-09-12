package com.luopc.platform.cloud.web.exception;

public class TimeoutFallbackException extends BaseException {

    public TimeoutFallbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
