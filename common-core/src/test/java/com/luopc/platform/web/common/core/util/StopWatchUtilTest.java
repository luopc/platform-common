package com.luopc.platform.web.common.core.util;

import lombok.extern.slf4j.Slf4j;

/**
 * StopWatchUtil工具类测试
 * 验证场景：单方法统计、嵌套分段统计、异常场景兼容
 */

@Slf4j
public class StopWatchUtilTest {

    public static void main(String[] args) {
        // 测试1：带返回值的单方法统计
        testSingleMethodWithReturn();

        // 测试2：无返回值的单方法统计
        testSingleMethodWithoutReturn();

        // 测试3：嵌套分段统计（下单流程）
        testNestedMultiTask();
    }

    /**
     * 测试：带返回值的单方法统计
     */
    private static void testSingleMethodWithReturn() {
        String result = StopWatchUtil.time("获取用户昵称", () -> {
            // 模拟业务耗时（100毫秒）
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "张三";
        });
        log.info("测试1 - 带返回值统计：返回结果={}", result);
    }

    /**
     * 测试：无返回值的单方法统计
     */
    private static void testSingleMethodWithoutReturn() {
        StopWatchUtil.time("打印用户日志", () -> {
            // 模拟业务耗时（50毫秒）
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("测试2 - 无返回值统计：用户日志打印完成");
        });
    }

    /**
     * 测试：嵌套分段统计（下单流程）
     */
    private static void testNestedMultiTask() {
        StopWatchUtil.multi("下单流程", watch -> {
            // 子任务1：参数校验
            watch.run("参数校验", () -> {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // 子任务2：库存扣减（嵌套子任务）
            watch.run("库存扣减", () -> {
                watch.run("查询库存", () -> {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                watch.run("扣减库存", () -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });

            // 子任务3：生成订单
            watch.run("生成订单", () -> {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
