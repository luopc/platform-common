package com.luopc.platform.web.common.core.env;

import static com.luopc.platform.web.common.core.constant.LoggingConstants.*;

import com.luopc.platform.web.common.core.util.SimpleTicketGeneratorUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

@Slf4j
@Setter
@Order(Ordered.LOWEST_PRECEDENCE)
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
        } else {
            appName = System.getProperty("spring.application.name");
            if (Objects.nonNull(appName)) {
                System.setProperty(APP_KEY, appName);
            }
        }

        String envName = environment.getProperty("logging.config.env-name");
        if (Objects.nonNull(envName)) {
            System.setProperty(ENV_KEY, envName);
        } else {
            String profile = System.getProperty("spring.profiles.active");
            if (Objects.nonNull(profile)) {
                System.setProperty(ENV_KEY, profile);
            }
        }

        String instanceName = environment.getProperty("logging.config.instance-name");
        if (Objects.nonNull(instanceName)) {
            System.setProperty(INSTANCE_KEY, instanceName);
        } else  {
            System.setProperty(INSTANCE_KEY, SimpleTicketGeneratorUtil.shortUuid());
        }

        String groupName = environment.getProperty("logging.config.group-name");
        if (Objects.nonNull(groupName)) {
            System.setProperty(GROUP_KEY, groupName);
        }else  {
            System.setProperty(GROUP_KEY, "default");
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
