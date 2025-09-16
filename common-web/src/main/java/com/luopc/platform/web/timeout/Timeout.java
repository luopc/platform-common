package com.luopc.platform.web.timeout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/nihao2q/article/details/147229344?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-147229344-blog-131813453.235^v43^pc_blog_bottom_relevance_base7&spm=1001.2101.3001.4242.1&utm_relevant_index=2
 * https://mp.weixin.qq.com/s?__biz=MzA5MzI5NjQxNQ==&mid=2447622559&idx=1&sn=3123267b0b0473651cb9cef8bfba7d49&scene=21&poc_token=HGosxGijUef6IAUKcrYalQZwRdhAZuiut_21ndGa
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    // 基础超时时间（支持 SpEL 表达式，如 "${pack.app.xxx.timeout}"）
    String value() default "5000";
    // 时间单位
    TimeUnit unit() default TimeUnit.MILLISECONDS ;
    // 重试次数（默认不重试）
    int retry() default 0;
    // 重试间隔（毫秒）
    long retryDelay() default 0 ;
    // 降级方法名（需在同一类中）
    String fallback() default "";
    // 线程池名称（指向配置的线程池 Bean）
    String executor() default "timeoutExecutor";
}
