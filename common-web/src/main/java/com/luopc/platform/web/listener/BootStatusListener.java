package com.luopc.platform.web.listener;

import com.luopc.platform.web.common.core.env.BootEnvironment;
import com.luopc.platform.web.common.core.env.BootResilience;
import com.luopc.platform.web.common.core.resilience.HealthCheckResult;
import com.luopc.platform.web.common.core.resilience.checkers.HotWarmHealthChecker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.luopc.platform.web.common.core.constant.LoggingConstants.INSTANCE_KEY;

@Slf4j
@Component
public class BootStatusListener implements InitializingBean, ApplicationRunner, DisposableBean {
    private final ScheduledExecutorService healthCheckExecutorService = Executors.newSingleThreadScheduledExecutor();
    private HotWarmHealthChecker hotWarmHealthChecker;

    @Resource
    protected BootEnvironment bootEnvironment;
    @Resource
    protected BootResilience bootResilience;

    @Override
    public void afterPropertiesSet() throws Exception {
        healthCheck();
        // init instance
        doInit();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // start
        if (!bootResilience.isHotWarmModel()) {
            doStart();
        } else {
            log.info("Service is hot-warm model, running after healthcheck.");
        }
    }

    private void doInit() {
        String instanceKey = System.getProperty(INSTANCE_KEY);
        log.info("Service Initialize completed - {}", instanceKey);
    }


    private void doStart() {
        printStatus("SERVICE ACTIVATION COMPLETED [" + bootEnvironment.getCurrentEnvironment() + "]");
        bootResilience.setToActive();
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
        bootResilience.setToStandby();
    }

    private void healthCheck() {
        if (bootResilience.isHotWarmModel()) {
            hotWarmHealthChecker = new HotWarmHealthChecker();
            healthCheckExecutorService.scheduleAtFixedRate(() -> {
                        try {
                            HealthCheckResult result = hotWarmHealthChecker.check();
                            log.info("Health Check Result: {}", result);
                            if (result.isSuccess()) {
                                if (!bootResilience.isActive()) {
                                    doInit();
                                    doStart();
                                } else {
                                    log.info("Service currently is active, do nothing...");
                                }
                            } else {
                                if (!bootResilience.isStandby()) {
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
                    bootResilience.getInitialDelaySec(),
                    bootResilience.getHealthCheckIntervalSec(),
                    TimeUnit.SECONDS);
        }
    }

    private void printStatus(String status) {
        String f = "\t".repeat(4);
        String s = "=".repeat(100);
        log.info("\n{}\n{}\n{}", s, f + status, s);
    }
}
