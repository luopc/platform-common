package com.luopc.platform.cloud.web.exception.log;

import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class AppErrorCodeLoggerFactory {

    public static AppErrorCodeLogger getLogger(Class<?> clazz) {
        return new AppErrorCodeLogger(LoggerFactory.getLogger(clazz));
    }

    public static AppErrorCodeLogger getLogger(String name) {
        return new AppErrorCodeLogger(LoggerFactory.getLogger(name));
    }

}
