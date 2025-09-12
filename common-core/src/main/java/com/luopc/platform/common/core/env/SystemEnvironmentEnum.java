package com.luopc.platform.common.core.env;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemEnvironmentEnum {
    /**
     * dev
     */
    DEV(SystemEnvironmentNameConst.DEV, "开发环境"),

    /**
     * test
     */
    UAT(SystemEnvironmentNameConst.UAT, "测试环境"),
    SUP(SystemEnvironmentNameConst.SUP, "测试环境"),

    /**
     * pre
     */
    PRE(SystemEnvironmentNameConst.PRE, "预发布环境"),

    /**
     * prod
     */
    PRD(SystemEnvironmentNameConst.PRD, "生产环境");

    private final String value;

    private final String desc;

    public static final class SystemEnvironmentNameConst {
        public static final String DEV = "dev";
        public static final String UAT = "uat";
        public static final String SUP = "sup";
        public static final String PRE = "pre";
        public static final String PRD = "prd";
    }
}
