package com.luopc.platform.web.common.core.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class BootResilience {

    @Value("${platform.resilience.isHotWarmModel:false}")
    private boolean isHotWarmModel;
    @Value("${platform.resilience.initialDelaySec:5}")
    private long initialDelaySec;
    @Value("${platform.resilience.healthCheckIntervalSec:10}")
    private long healthCheckIntervalSec;

    private volatile boolean isActive = false;
    private volatile boolean isStandby = true;

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
