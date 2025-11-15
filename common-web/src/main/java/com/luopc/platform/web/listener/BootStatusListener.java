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
    private static final long INITIAL_DELAY_MS = Duration.ofSeconds(30).toMillis();
    private HotWarmHealthChecker hotWarmHealthChecker;

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
            hotWarmHealthChecker = new HotWarmHealthChecker();
            healthCheckExecutorService.scheduleAtFixedRate(() -> {
                        try {
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
                        } catch (Exception e) {
                            log.error("Health check task failed", e);
                        }
                    },
                    INITIAL_DELAY_MS,
                    bootEnvironment.getHealthCheckInterval(),
                    TimeUnit.MILLISECONDS);
        }
    }

    private void printStatus(String status) {
        String f = "\t".repeat(25);
        String s = "=".repeat(225);
        log.info("\n{}\n{}\n{}", s, f + status, s);
    }
}
