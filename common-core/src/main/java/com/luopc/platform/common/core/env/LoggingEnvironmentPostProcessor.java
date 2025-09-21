package com.luopc.platform.common.core.env;

import static com.luopc.platform.common.core.constant.LoggingConstants.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

@Slf4j
@Setter
public class LoggingEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private int order = 100;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("LoggingEnvironmentPostProcessor postProcessEnvironment running...");
        String logPath = environment.getProperty("logging.config.log-path");
        if (Objects.nonNull(logPath)) {
            System.setProperty(LOGGER_PATH, logPath);
        }

        String appName = environment.getProperty("logging.config.app-name");
        if (Objects.nonNull(appName)) {
            System.setProperty(APP_KEY, appName);
        }

        String envName = environment.getProperty("logging.config.env-name");
        if (Objects.nonNull(envName)) {
            System.setProperty(ENV_KEY, envName);
        }

        String groupName = environment.getProperty("logging.config.group-name");
        if (Objects.nonNull(groupName)) {
            System.setProperty(GROUP_KEY, groupName);
        }

        String instanceName = environment.getProperty("logging.config.instance-name");
        if (Objects.nonNull(instanceName)) {
            System.setProperty(INSTANCE_KEY, instanceName);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
