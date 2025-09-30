package com.luopc.platform.common.core.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 增强版时间格式化工具类
 * 支持中英文和自定义格式
 */
@UtilityClass
public class TimeFormatterEx {

    private static final long MILLIS_PER_SECOND = 1000L;
    private static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    private static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    private static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    // 中文单位
    private static final String[] CHINESE_UNITS = {"天", "小时", "分钟", "秒", "毫秒"};
    // 英文单位
    private static final String[] ENGLISH_UNITS = {"d", "h", "m", "s", "ms"};
    // 英文全称单位
    private static final String[] ENGLISH_FULL_UNITS = {"day", "hour", "minute", "second", "millisecond"};

    public enum Language {
        CHINESE, ENGLISH, ENGLISH_FULL
    }

    /**
     * 格式化毫秒（中文）
     */
    public static String formatMillis(long millis) {
        return formatDuration(millis, TimeUnit.MILLISECONDS, Language.ENGLISH);
    }

    /**
     * 格式化秒（中文）
     */
    public static String formatSeconds(long seconds) {
        return formatDuration(seconds, TimeUnit.SECONDS, Language.ENGLISH);
    }

    /**
     * 格式化时间间隔
     */
    public static String formatDuration(long duration, TimeUnit unit, Language language) {
        long totalMillis = unit.toMillis(duration);

        if (totalMillis == 0) {
            return getZeroDisplay(language);
        }

        boolean negative = totalMillis < 0;
        totalMillis = Math.abs(totalMillis);

        long days = totalMillis / MILLIS_PER_DAY;
        long hours = (totalMillis % MILLIS_PER_DAY) / MILLIS_PER_HOUR;
        long minutes = (totalMillis % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
        long seconds = (totalMillis % MILLIS_PER_MINUTE) / MILLIS_PER_SECOND;
        long millis = totalMillis % MILLIS_PER_SECOND;

        List<String> parts = new ArrayList<>();

        if (days > 0) parts.add(days + getUnit(0, language, days > 1));
        if (hours > 0) parts.add(hours + getUnit(1, language, hours > 1));
        if (minutes > 0) parts.add(minutes + getUnit(2, language, minutes > 1));
        if (seconds > 0) parts.add(seconds + getUnit(3, language, seconds > 1));
        if (millis > 0 && totalMillis < MILLIS_PER_MINUTE) {
            parts.add(millis + getUnit(4, language, millis > 1));
        }

        if (parts.isEmpty()) {
            return getZeroDisplay(language);
        }

        String result = String.join("", parts);
        return negative ? "-" + result : result;
    }

    /**
     * 简洁格式（自动选择最大单位）
     */
    public static String formatDurationConcise(long duration, TimeUnit unit, Language language) {
        double totalMillis = (double) unit.toMillis(duration);

        if (totalMillis == 0) {
            return getZeroDisplay(language);
        }

        boolean negative = totalMillis < 0;
        totalMillis = Math.abs(totalMillis);

        String result;
        if (totalMillis >= MILLIS_PER_DAY) {
            double days = totalMillis / MILLIS_PER_DAY;
            result = formatDecimal(days) + getUnit(0, language, days != 1);
        } else if (totalMillis >= MILLIS_PER_HOUR) {
            double hours = totalMillis / MILLIS_PER_HOUR;
            result = formatDecimal(hours) + getUnit(1, language, hours != 1);
        } else if (totalMillis >= MILLIS_PER_MINUTE) {
            double minutes = totalMillis / MILLIS_PER_MINUTE;
            result = formatDecimal(minutes) + getUnit(2, language, minutes != 1);
        } else if (totalMillis >= MILLIS_PER_SECOND) {
            double seconds = totalMillis / MILLIS_PER_SECOND;
            result = formatDecimal(seconds) + getUnit(3, language, seconds != 1);
        } else {
            result = (long) totalMillis + getUnit(4, language, totalMillis != 1);
        }

        return negative ? "-" + result : result;
    }

    /**
     * 获取单位字符串
     */
    private static String getUnit(int index, Language language, boolean plural) {
        switch (language) {
            case CHINESE:
                return CHINESE_UNITS[index];
            case ENGLISH:
                return ENGLISH_UNITS[index];
            case ENGLISH_FULL:
                String unit = ENGLISH_FULL_UNITS[index];
                return plural ? unit + "s" : unit;
            default:
                return CHINESE_UNITS[index];
        }
    }

    /**
     * 获取零值的显示
     */
    private static String getZeroDisplay(Language language) {
        switch (language) {
            case CHINESE:
                return "0秒";
            case ENGLISH:
                return "0s";
            case ENGLISH_FULL:
                return "0 seconds";
            default:
                return "0秒";
        }
    }

    /**
     * 格式化小数
     */
    private static String formatDecimal(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            // 尝试保留1位小数，如果小数部分为0则去掉
            String formatted = String.format("%.1f", value);
            if (formatted.endsWith(".0")) {
                return formatted.substring(0, formatted.length() - 2);
            }
            return formatted;
        }
    }

    /**
     * 为性能监控设计的格式化方法
     */
    public static String formatForPerformance(long duration, TimeUnit unit) {
        long millis = unit.toMillis(duration);

        if (millis < 1000) {
            return millis + "ms";
        } else if (millis < 60000) {
            return String.format("%.1fs", millis / 1000.0);
        } else if (millis < 3600000) {
            long minutes = millis / 60000;
            long seconds = (millis % 60000) / 1000;
            return seconds > 0 ? minutes + "m" + seconds + "s" : minutes + "m";
        } else {
            long hours = millis / 3600000;
            long minutes = (millis % 3600000) / 60000;
            return minutes > 0 ? hours + "h" + minutes + "m" : hours + "h";
        }
    }
}
