package com.luopc.platform.web.common.core.util.os;

import lombok.Data;

/**
 * 系统信息
 *
 */
@Data
public class OSInfo {

    /**
     * 系统
     */
    private String os;
    /**
     * 系统类型
     */
    private String family;
    /**
     * 系统架构
     */
    private String osArch;

    /**
     * java版本
     */
    private String javaRuntimeName;
    private String javaVersion;
    private String javaVmVendor;

    /**
     * 工作目录
     */
    private String userDir;

    /**
     * cpu核心数
     */
    private int cpuCount;
    private int physicalCoresCount;

    /**
     * 主机host
     */
    private String host;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 系统启动时间
     */
    private String bootTime;
}


