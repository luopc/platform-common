package com.luopc.platform.web.result;

import com.luopc.platform.common.core.exception.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 返回消息公共类:https://mp.weixin.qq.com/s/pDdSCxsQeq-J80yqi4-Gxg
 */
public class ResponseMessage<T> {

    private String code = "200";      // 业务状态码

    private String message;  // 业务消息

    private T data;          // 响应数据

    // 私有构造器（使用Builder模式）
    private ResponseMessage(Builder<T> builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    // 返回业务状态默认
    public static <T> ResponseMessage<T> success() {
        return new Builder<T>()
                .code("200")
                .message("Success")
                .build();
    }

    // 只设置返回数据，业务状态默认
    public static <T> ResponseMessage<T> success(T data) {
        return new Builder<T>()
                .code("200")
                .message("Success")
                .data(data)
                .build();
    }

    // 只设置返回信息，业务状态默认
    public static <T> ResponseMessage<T> success(String message) {
        return new Builder<T>()
                .code("200")
                .message(message)
                .build();
    }

    // 错误的业务状态，返回信息自定义
    public static <T> ResponseMessage<T> error(String code, String message) {
        return new Builder<T>()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ResponseMessage<T> error(ErrorCode errorCode) {
        return new Builder<T>()
                .code(String.valueOf(errorCode.getCode()))
                .message(errorCode.getMessage())
                .build();
    }

    // --------------- 转换回原始ResponseEntity ---------------
    public ResponseEntity<ResponseMessage<T>> toResponseEntity() {
        // 返回HttpStatus200和默认头部
        return toResponseEntity(HttpStatus.OK, new HttpHeaders());
    }

    public ResponseEntity<ResponseMessage<T>> toResponseEntity(HttpStatus httpStatus) {
        // 设置其他HttpStatus状态，默认头部
        return toResponseEntity(httpStatus, new HttpHeaders());
    }

    public ResponseEntity<ResponseMessage<T>> toResponseEntity(HttpStatus httpStatus, HttpHeaders headers) {
        return new ResponseEntity<>(this, headers, httpStatus);
    }

    // --------------- Builder 类 ---------------
    public static class Builder<T> {

        private String code = "200";

        private String message = null;

        private T data = null;

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseMessage<T> build() {
            return new ResponseMessage<>(this);
        }
    }

    // --------------- 响应体结构 ---------------
    public static class ResponseBody<T> {

        private final String code;

        private final String message;

        private final T data;

        public ResponseBody(String code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // Getter 方法（JSON序列化需要）
        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }

    // --------------- Getter 方法 ---------------
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
