package com.luopc.platform.web.listener;

import com.luopc.platform.common.core.env.BootEnvironment;
import com.luopc.platform.common.core.resilience.HealthCheckResult;
import com.luopc.platform.common.core.resilience.checkers.HotWarmHealthChecker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BootStatusListener implements InitializingBean, ApplicationRunner, DisposableBean {
    private final ScheduledExecutorService healthCheckExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Resource
    protected BootEnvironment bootEnvironment;

    @Override
    public void afterPropertiesSet() throws Exception {
        healthCheck();
        // init instance
        doInit();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // start
        if (!bootEnvironment.isHotWormModel()) {
            doStart();
        }
    }

    private void doInit() {
        log.info("Service Initialize completed");
    }


    private void doStart() {
        printStatus("SERVICE ACTIVATION COMPLETED [" + bootEnvironment.getCurrentEnvironment() + "]");
        bootEnvironment.getResilience().setToActive();
    }

    @Override
    public void destroy() throws Exception {
        // dispose
        doDispose();

        // stop
        doStop();
    }

    private void doDispose() {
        log.info("Service disposed completed");
    }

    private void doStop() {
        printStatus("SERVICE SHUTDOWN COMPLETED [" + bootEnvironment.getCurrentEnvironment() + "]");
        bootEnvironment.getResilience().setToStandby();
    }

    private void healthCheck() {
        if (bootEnvironment.isHotWormModel()) {
            HotWarmHealthChecker hotWarmHealthChecker = new HotWarmHealthChecker();
            healthCheckExecutorService.scheduleAtFixedRate(() -> {
                        HealthCheckResult result = hotWarmHealthChecker.check();
                        log.info("Health Check Result: {}", result);
                        if (result.isSuccess()) {
                            if (!bootEnvironment.getResilience().isActive()) {
                                doInit();
                                doStart();
                            } else {
                                log.info("Service currently is active, do nothing...");
                            }
                        } else {
                            if (!bootEnvironment.getResilience().isStandby()) {
                                doDispose();
                                doStop();
                            } else {
                                log.info("Service currently is standby, do nothing...");
                            }
                        }
                    },
                    Duration.ofSeconds(30).toMillis(),
                    bootEnvironment.getHealthCheckInterval(),
                    TimeUnit.MILLISECONDS);
        }
    }

    private void printStatus(String status) {
        log.info("\n{}\n{}{}\n{}", "=".repeat(200), "\t".repeat(25), status, "=".repeat(200));
    }
}
