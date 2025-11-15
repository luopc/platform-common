package com.luopc.platform.common.core.env;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Objects;

@Getter
public class BootEnvironment {

    @Setter
    private BootResilience resilience;
    /**
     * 是否为生产环境
     */
    private final boolean isProd;
    /**
     * 是否为开发环境
     */
    private final boolean isDev;
    /**
     * 是否为测试环境
     */
    private final boolean isTest;

    /**
     * 项目名称
     */
    private final String projectName;

    /**
     * 当前环境
     */
    private final BootEnvironmentEnum currentEnvironment;

    public BootEnvironment(String projectName, BootEnvironmentEnum currentEnvironment) {
        this.isProd = BootEnvironmentEnum.PRD.equals(currentEnvironment);
        this.isDev = BootEnvironmentEnum.DEV.equals(currentEnvironment);
        this.isTest = (BootEnvironmentEnum.UAT.equals(currentEnvironment)
                || BootEnvironmentEnum.SUP.equals(currentEnvironment)
                || BootEnvironmentEnum.PRE.equals(currentEnvironment));
        this.projectName = projectName;
        this.currentEnvironment = currentEnvironment;
        //30 分钟做一次health check
        this.resilience = new BootResilience(Duration.ofSeconds(30).toMillis());
    }

    public long getHealthCheckInterval() {
        if (Objects.nonNull(resilience)) return resilience.getHealthCheckInterval();
        return 0;
    }

    @Override
    public String toString() {
        return "SystemEnvironment: current env is [" + currentEnvironment + "], Resilience Status is " + resilience + ".";
    }

    public boolean isHotWormModel() {
        if (Objects.nonNull(resilience)) return resilience.isHotWormModel();
        return false;
    }
}
