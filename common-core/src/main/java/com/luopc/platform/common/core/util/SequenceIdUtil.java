package com.luopc.platform.common.core.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by michaelliao / itranswarp
 * @className IdUtil
 * @description SequenceIdProvider
 * @date 2024/1/14 0014 9:50
 * 53 bits unique id:
 * <p>
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
 * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
 * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
 * <p>
 * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
 * <p>
 * Maximum TS = 11111_11111111_11111111_11111111_111
 * <p>
 * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
 * <p>
 * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
 * <p>
 * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
 */
@Slf4j
public class SequenceIdUtil {

    private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

    private static final long OFFSET = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    private static final long MAX_NEXT = 0b11111_11111111_111L;

//    private final static long SHARD_ID = 2;

    private static long offset = 0;

    private static long lastEpoch = 0;

    private static String SHORT_ID_PREFIX;
    private static AtomicInteger SHORT_ID_NEXT = new AtomicInteger(0);
    private final static Integer SHORT_ID_MAX = 9999;
    private final static Table<String, Integer, Integer> SHORT_ID_RENTAL = HashBasedTable.create();

    public static long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取机器ID ,copy至mybatis-plus
     *
     * @param maxWorkerId 最大ID
     * @return workId
     */
    public static long getMaxWorkerId(long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }


    /**
     * 根据当前时间生成短ID, 如超过最大ID数则非顺序ID
     * 如果某一个分钟的ID 超过最大值，则向下一个分钟借
     * 其中60-99分钟作为冗余，每分钟能借9999个ID，共可借389,961个
     * 每小时能生成的ID数据为： 99*9999=989,901个ID
     *
     * @return 顺序短ID
     */
    public static Long shortId() {
        // 取年份后两位  和  一年中的第几个小时作为前缀，一年共分成365*24=8760个小时
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear()).substring(2);
        //一年分成8760个小时，取当前的小时数
        String dayHours = String.format("%03d", now.getDayOfYear()) +  String.format("%02d", now.getHour());
        String yearAnHour = year + dayHours;
        StringBuilder prefix = new StringBuilder(yearAnHour);
        String workId = String.valueOf(getMaxWorkerId(9));
        //判断之前有无租借过ID，没有则在前缀后面添加当前的分钟数：如01， 和序列ID： 如 001
        if (SHORT_ID_RENTAL.row(yearAnHour).isEmpty()) {
            SHORT_ID_RENTAL.clear();

            //cache 为空，则走正常处理流程
            String minutes = getShortMinuteId(now.getMinute());
            prefix.append(minutes);

            //判断当前的时分是否已经成功过ID，如果没有则从1开始，如果有则取上一次的+1
            if (prefix.toString().equals(SHORT_ID_PREFIX)) {
                SHORT_ID_NEXT.getAndIncrement();
                //如超过序列ID超过三位数，则向下一分钟借，并存入缓存
                if (SHORT_ID_NEXT.get() > SHORT_ID_MAX) {
                    // 向下一分钟借
                    String rentalId = rentalId(now.getMinute() + 1, yearAnHour);
                    // 年时前缀 + 借来的ID 返回作为当前ID
                    return Long.parseLong(yearAnHour + rentalId + workId);
                }
            } else {
                SHORT_ID_PREFIX = prefix.toString();
                SHORT_ID_NEXT = new AtomicInteger(0);
            }
            prefix.append(getNextShortSeqId(SHORT_ID_NEXT.get())).append(workId);
            return Long.parseLong(prefix.toString());

        } else {
            // cache不为空，则判断当前的分钟数是否已存在被占用ID
            String rentalId = rentalId(now.getMinute(), yearAnHour);
            return Long.parseLong(yearAnHour + rentalId + workId);
        }
    }

    private static String rentalId(int rentalMinute, String yearAnHour) {
        StringBuilder result = new StringBuilder();
        Map<Integer, Integer> seqIdWithMinutes = SHORT_ID_RENTAL.row(yearAnHour);
        Optional<Integer> maxMinutes = seqIdWithMinutes.keySet().stream().max(Integer::compareTo);

        int currentIndex = 1;
        if (maxMinutes.isPresent()) {
            if (rentalMinute > maxMinutes.get()) {
                //当前分钟数>cache的值，清空过期的cache
                SHORT_ID_RENTAL.clear();
                SHORT_ID_PREFIX = yearAnHour + getShortMinuteId(rentalMinute);
                SHORT_ID_NEXT = new AtomicInteger(0);
            } else {
                rentalMinute = maxMinutes.get();
                if (rentalMinute > 99) {
                    log.error("超过每小时最大ID 生成数，请检查流量是否正常, invalid minute = {}", rentalMinute);
                    //返回比正常ID多一位的随机ID，未去重，未排序
                    return String.format("%07d", SmartNumberUtil.randomNumber(0, 999999));
                } else {
                    currentIndex = seqIdWithMinutes.get(maxMinutes.get()) + 1;
                    if (currentIndex > SHORT_ID_MAX) {
                        //超过最大值，继续向下一个分钟数借
                        rentalMinute = maxMinutes.get() + 1;
                        currentIndex = 1;
                    }
                }
            }
        }
        SHORT_ID_RENTAL.put(yearAnHour, rentalMinute, currentIndex);
        String minutes = getShortMinuteId(rentalMinute);
        String nextSeqId = getNextShortSeqId(currentIndex);
        return result.append(minutes).append(nextSeqId).toString();
    }

    private static String getShortMinuteId(int minute) {
        return String.format("%02d", minute);
    }

    private static String getNextShortSeqId(int seqNum) {
        return String.format("%04d", seqNum);
    }

    private static synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            // warning: clock is turn back:
            log.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
            epochSecond = lastEpoch;
        }
        if (lastEpoch != epochSecond) {
            lastEpoch = epochSecond;
            reset();
        }
        offset++;
        long next = offset & MAX_NEXT;
        if (next == 0) {
            log.warn("maximum id reached in 1 second in epoch: " + epochSecond);
            return nextId(epochSecond + 1);
        }
        //log.info("epochSecond={}", epochSecond);
        return generateId(epochSecond, next, getMaxWorkerId(99));
    }

    private static void reset() {
        offset = 0;
    }

    private static long generateId(long epochSecond, long next, long shardId) {
        return ((epochSecond - OFFSET) << 21) | (next << 5) | shardId;
    }

    public static long stringIdToLongId(String stringId) {
        // a stringId id is composed as timestamp (15) + uuid (32) + serverId (000~fff).
        Matcher matcher = PATTERN_LONG_ID.matcher(stringId);
        if (matcher.matches()) {
            long epoch = Long.parseLong(matcher.group(1)) / 1000;
            String uuid = matcher.group(2);
            byte[] sha1 = HashUtil.sha1AsBytes(uuid);
            long next = ((sha1[0] << 24) | (sha1[1] << 16) | (sha1[2] << 8) | sha1[3]) & MAX_NEXT;
            long serverId = Long.parseLong(matcher.group(3), 16);
            return generateId(epoch, next, serverId);
        }
        throw new IllegalArgumentException("Invalid id: " + stringId);
    }
}
