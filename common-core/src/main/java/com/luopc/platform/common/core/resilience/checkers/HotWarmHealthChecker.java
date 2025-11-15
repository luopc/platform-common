package com.luopc.platform.common.core.resilience.checkers;

import com.luopc.platform.common.core.resilience.HealthCheckResult;
import com.luopc.platform.common.core.resilience.HealthChecker;
import com.luopc.platform.common.core.util.SmartNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotWarmHealthChecker implements HealthChecker {

    @Override
    public String getCheckItem() {
        return "Hot-Warm";
    }

    @Override
    public HealthCheckResult check() {
        HealthCheckResult result = new HealthCheckResult();
        result.setCheckItem(getCheckItem());
        long startTime = System.currentTimeMillis();
        result.setSuccess(checkResilience());
        result.setCostTime(System.currentTimeMillis() - startTime);
        return result;
    }

    private boolean checkResilience() {
        //TODO - implement checking logic
        return SmartNumberUtil.nextInt(100) % 2 == 0;
    }

}
