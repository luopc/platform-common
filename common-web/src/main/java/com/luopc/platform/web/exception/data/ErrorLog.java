package com.luopc.platform.web.exception.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorLog {

    private String uri;
    private String method;
    private String message;
    private String stackTrace;
    private LocalDateTime createTime;
}
