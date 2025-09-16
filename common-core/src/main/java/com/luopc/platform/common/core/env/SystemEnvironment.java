package com.luopc.platform.common.core.env;

import lombok.Getter;

@Getter
public class SystemEnvironment {

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
    private final SystemEnvironmentEnum currentEnvironment;

    public SystemEnvironment(String projectName, SystemEnvironmentEnum currentEnvironment) {
        this.isProd = SystemEnvironmentEnum.PRD.equals(currentEnvironment);
        this.isDev = SystemEnvironmentEnum.DEV.equals(currentEnvironment);
        this.isTest = (SystemEnvironmentEnum.UAT.equals(currentEnvironment)
                || SystemEnvironmentEnum.SUP.equals(currentEnvironment)
                || SystemEnvironmentEnum.PRE.equals(currentEnvironment));
        this.projectName = projectName;
        this.currentEnvironment = currentEnvironment;
    }
}
