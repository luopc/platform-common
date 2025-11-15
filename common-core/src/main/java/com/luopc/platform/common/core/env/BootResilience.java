package com.luopc.platform.common.core.env;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BootResilience {

    private boolean isHotWormModel;
    private long initialDelayMs;
    private long healthCheckIntervalMs;
    private volatile boolean isActive;
    private volatile boolean isStandby;


    public BootResilience(long healthCheckIntervalMs) {
        this.healthCheckIntervalMs = healthCheckIntervalMs;
        this.isActive = false;
        this.isStandby = true;
    }

    public void setToActive() {
        this.isActive = true;
        this.isStandby = false;
    }

    public void setToStandby() {
        this.isActive = false;
        this.isStandby = true;
    }

    @Override
    public String toString() {
        return "isActive=" + isActive +
                ", isStandby=" + isStandby;
    }
}
