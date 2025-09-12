package com.luopc.platform.cloud.common.process;

import com.luopc.platform.cloud.common.process.config.ProcessConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(ProcessConfig.class)
public class VMagentApplication {
    public static void main(String[] args) {
        SpringApplication.run(VMagentApplication.class, args);
    }
}
