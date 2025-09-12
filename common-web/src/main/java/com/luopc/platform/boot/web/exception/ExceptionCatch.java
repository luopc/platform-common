package com.luopc.platform.boot.web.exception;

import com.google.common.collect.ImmutableMap;
import com.luopc.platform.common.core.exception.ErrorCode;
import com.luopc.platform.common.core.exception.PlatformErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Objects;

/**
 * 定义一个只读的map存储异常类型的错误代码的映射，map中没有的元素，统一用错误代码500来定义。
 * 参考： <a href="https://mp.weixin.qq.com/s/OJwihsyy-vS_va98y-xBjw">从阿里跳槽来的工程师，写个try catch的方式都这么优雅！</a> 进行重构
 */
@Slf4j
public class ExceptionCatch {

    /**
     * 定义map，存贮常见错误信息。该类map不可修改
     */
    private static ImmutableMap<Class<? extends Throwable>, ErrorCode> EXCEPTIONS;
    /**
     * 构建ImmutableMap
     */
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ErrorCode> builder = ImmutableMap.builder();


    static {
        builder.put(HttpMessageNotReadableException.class, PlatformErrorCode.NOT_READABLE);
    }

    /**
     * 捕获非自定义类异常
     *
     * @param exception 异常
     * @return ResponseDTO
     */
    public static ResponseEntity<?> exception(Exception exception) {
        // 记录日志
        log.error("catch exception ==> ", exception);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        ErrorCode resultCode = EXCEPTIONS.get(exception.getClass());
        return ResponseEntity.internalServerError().body(Objects.requireNonNullElse(resultCode, PlatformErrorCode.SYSTEM_ERROR));
    }

}
