package com.luopc.platform.web.config;

import com.luopc.platform.common.core.env.BootEnvironment;
import com.luopc.platform.common.core.env.BootEnvironmentEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 系统环境
 */
@Slf4j
@Configuration
public class BootSystemEnvironmentConfig implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Value("${spring.application.name}")
    private String projectName;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        BootEnvironmentEnum activeEnv = EnumUtils.getEnumIgnoreCase(BootEnvironmentEnum.class, property);
        return StringUtils.isNotBlank(property) && !BootEnvironmentEnum.PRD.equals(activeEnv);
    }

    @Bean
    public BootEnvironment initEnvironment() {
        log.info("init system environment - {}", systemEnvironment);
        BootEnvironmentEnum currentEnvironment = EnumUtils.getEnumIgnoreCase(BootEnvironmentEnum.class, systemEnvironment);
        if (currentEnvironment == null) {
            throw new ExceptionInInitializerError("无法获取当前环境！请在 application.yaml 配置参数：spring.profiles.active");
        }
        if (StringUtils.isBlank(projectName)) {
            throw new ExceptionInInitializerError("无法获取当前项目名称！请在 application.yaml 配置参数：spring.application.name");
        }

        return new BootEnvironment(projectName, currentEnvironment);
    }
}
