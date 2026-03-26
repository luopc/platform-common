package com.luopc.platform.web.common.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 耗时统计工具类 - 基础版
 * 核心能力：单方法耗时统计，支持带返回值/无返回值，自动打印日志
 */
public final class StopWatchUtil {

    // 日志组件，替代System.out.println，符合生产环境规范
    private static final Logger log = LoggerFactory.getLogger(StopWatchUtil.class);

    // 私有构造器，禁止实例化工具类
    private StopWatchUtil() {
    }

    /**
     * 带返回值的方法耗时统计
     *
     * @param taskName 任务名称（用于日志标识）
     * @param supplier 业务逻辑回调（带返回值）
     * @param <T>      返回值泛型
     * @return 业务逻辑的返回值
     */
    public static <T> T time(String taskName, Supplier<T> supplier) {
        // 初始化StopWatch，指定任务名称（用于日志/统计标识）
        StopWatch stopWatch = new StopWatch(taskName);
        // 启动计时
        stopWatch.start();

        try {
            // 执行业务逻辑并返回结果
            return supplier.get();
        } finally {
            // 无论业务逻辑是否异常，都停止计时（保证统计准确性）
            stopWatch.stop();
            // 打印标准化耗时日志
            log.info("[StopWatch] {} 耗时: {} ms",
                    stopWatch.getId(),  // 任务名称
                    stopWatch.getTotalTimeMillis());  // 总耗时（毫秒）
        }
    }

    /**
     * 无返回值的方法耗时统计
     *
     * @param taskName 任务名称（用于日志标识）
     * @param runnable 业务逻辑回调（无返回值）
     */
    public static void time(String taskName, Runnable runnable) {
        StopWatch stopWatch = new StopWatch(taskName);
        stopWatch.start();

        try {
            // 执行无返回值的业务逻辑
            runnable.run();
        } finally {
            stopWatch.stop();
            // 统一日志格式，便于日志分析/监控告警
            log.info("[StopWatch] {} 耗时: {} ms",
                    stopWatch.getId(),
                    stopWatch.getTotalTimeMillis());
        }
    }

    // ======================== 增强版：嵌套分段统计 ========================

    /**
     * 多段/嵌套分段耗时统计入口
     *
     * @param taskName 总任务名称（如下单流程、支付流程）
     * @param consumer 分段任务回调（接收StopWatchWrapper，执行子任务）
     */
    public static void multi(String taskName, Consumer<StopWatchWrapper> consumer) {
        // 初始化总任务StopWatch
        StopWatch stopWatch = new StopWatch(taskName);
        // 封装包装类，处理嵌套逻辑
        StopWatchWrapper wrapper = new StopWatchWrapper(stopWatch);

        try {
            // 执行所有分段任务
            consumer.accept(wrapper);
        } finally {
            // 打印格式化的分段统计结果（含耗时占比）
            log.info("\n{}", stopWatch.prettyPrint());
        }
    }

    /**
     * StopWatch包装类：处理嵌套任务的暂停/恢复逻辑
     */
    public static class StopWatchWrapper {
        // 原生StopWatch对象
        private final StopWatch stopWatch;
        // 任务栈：记录嵌套任务名称，用于生成缩进前缀+管理任务层级
        private final Deque<String> taskStack = new ArrayDeque<>();

        private StopWatchWrapper(StopWatch stopWatch) {
            this.stopWatch = stopWatch;
        }

        /**
         * 无返回值的分段任务执行（嵌套场景）
         *
         * @param taskName 子任务名称
         * @param runnable 子任务业务逻辑
         */
        public void run(String taskName, Runnable runnable) {
            // 构建嵌套任务名称（添加缩进，便于区分层级）
            String fullTaskName = buildNestedName(taskName);
            // 执行核心逻辑（统一封装暂停/恢复逻辑）
            execute(fullTaskName, () -> {
                runnable.run();
                return null;
            });
        }

        /**
         * 带返回值的分段任务执行（嵌套场景）
         *
         * @param taskName 子任务名称
         * @param supplier 子任务业务逻辑（带返回值）
         * @param <T>      返回值泛型
         * @return 子任务返回值
         */
        public <T> T run(String taskName, Supplier<T> supplier) {
            String fullTaskName = buildNestedName(taskName);
            return execute(fullTaskName, supplier);
        }

        /**
         * 核心执行逻辑：处理任务的暂停/恢复+计时
         *
         * @param fullTaskName 完整任务名称（含缩进）
         * @param supplier     业务逻辑回调
         * @param <T>          返回值泛型
         * @return 业务逻辑返回值
         */
        private <T> T execute(String fullTaskName, Supplier<T> supplier) {
            // 标记当前是否有任务在运行（用于判断是否需要暂停父任务）
            boolean wasRunning = stopWatch.isRunning();
            // 记录当前运行的父任务名称（用于后续恢复）
            String pausedTaskName = wasRunning ? stopWatch.currentTaskName() : null;

            // 步骤1：如果有父任务在运行，先暂停（避免start冲突）
            if (wasRunning) {
                stopWatch.stop();
            }

            // 步骤2：启动当前子任务计时
            stopWatch.start(fullTaskName);
            // 将当前任务压入栈，记录层级
            taskStack.push(fullTaskName);

            try {
                // 步骤3：执行子任务业务逻辑
                return supplier.get();
            } finally {
                // 步骤4：停止当前子任务计时
                stopWatch.stop();
                // 将当前任务弹出栈，恢复层级
                taskStack.pop();

                // 步骤5：如果有暂停的父任务，恢复其计时
                if (wasRunning) {
                    stopWatch.start(pausedTaskName);
                }
            }
        }

        /**
         * 构建嵌套任务名称：根据层级添加缩进，提升日志/统计结果可读性
         * 例如：一级任务无缩进，二级任务加2个空格，三级加4个空格
         *
         * @param taskName 原始任务名称
         * @return 带缩进的完整任务名称
         */
        private String buildNestedName(String taskName) {
            // 获取当前嵌套层级（栈的大小）
            int depth = taskStack.size();
            StringBuilder prefix = new StringBuilder();
            // 每级嵌套添加2个空格缩进
            for (int i = 0; i < depth; i++) {
                prefix.append("  ");
            }
            // 拼接缩进+原始任务名称
            return prefix + taskName;
        }
    }
}
