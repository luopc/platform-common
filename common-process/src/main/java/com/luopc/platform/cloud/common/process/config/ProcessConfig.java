package com.luopc.platform.cloud.common.process.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.proc")
public class ProcessConfig {


    private String name;
    private String workPath;
    private String paramPrefix;
    private volatile Long runningPid;

    //start
    private String exec;
    private Map<String, String> parameters;

    //stop
    private String stopExec;
    private Map<String, String> stopParameters;
}
