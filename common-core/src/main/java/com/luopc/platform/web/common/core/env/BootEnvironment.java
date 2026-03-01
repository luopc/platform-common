package com.luopc.platform.web.common.core.env;

import lombok.Getter;

@Getter
public class BootEnvironment {

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
        this.isDev = BootEnvironmentEnum.DEV.equals(currentEnvironment)
                || BootEnvironmentEnum.LOCAL.equals(currentEnvironment);
        this.isTest = (BootEnvironmentEnum.UAT.equals(currentEnvironment)
                || BootEnvironmentEnum.SUP.equals(currentEnvironment)
                || BootEnvironmentEnum.PRE.equals(currentEnvironment));
        this.projectName = projectName;
        this.currentEnvironment = currentEnvironment;
    }


    @Override
    public String toString() {
        return "SystemEnvironment: current env is [" + currentEnvironment + "]";
    }

}
