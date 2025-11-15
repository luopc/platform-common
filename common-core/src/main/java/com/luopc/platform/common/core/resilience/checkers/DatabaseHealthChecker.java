package com.luopc.platform.common.core.resilience.checkers;

import com.luopc.platform.common.core.resilience.HealthCheckResult;
import com.luopc.platform.common.core.resilience.HealthChecker;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
public class DatabaseHealthChecker implements HealthChecker {

    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getCheckItem() {
        return "database";
    }

    @Override
    public HealthCheckResult check() {
        HealthCheckResult result = new HealthCheckResult();
        result.setCheckItem(getCheckItem());
        long startTime = System.currentTimeMillis();

        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(3)) { // 3秒超时检查连接有效性
                result.setSuccess(true);
            } else {
                log.warn("数据库连接无效");
                result.setSuccess(false);
                result.setErrorMsg("数据库连接无效");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            log.warn("数据库检查失败：{}", e.getMessage());
            result.setErrorMsg("数据库检查失败：" + e.getMessage());
        }

        result.setCostTime(System.currentTimeMillis() - startTime);
        return result;
    }
}
