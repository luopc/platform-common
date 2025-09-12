package com.luopc.platform.cloud.web.exception;

import com.google.common.collect.Lists;
import com.luopc.platform.common.core.exception.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class BaseException extends RuntimeException {

    private ErrorCode error;
    private List<Object> params = Lists.newArrayList();

    public BaseException() {
    }

    public BaseException withParam(Object param) {
        params.add(param);
        return this;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getCode() + ":" + errorCode.getMessage());
        this.error = errorCode;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
