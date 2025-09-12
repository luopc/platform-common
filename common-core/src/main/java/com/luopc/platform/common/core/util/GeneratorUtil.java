package com.luopc.platform.common.core.util;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author by Robin
 * @className GeneratorUtil
 * @description TODO
 * @date 2024/1/13 0013 0:00
 */
public class GeneratorUtil {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static String shortTicket() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear()).substring(2);
        //一年分成8760个小时，取当前的小时数
        String dayHours = String.format("%04d", (now.getDayOfYear() * now.getHour()));
        String yearAnHour = year + dayHours;
        return yearAnHour + shortUuid();
    }


    public static String longTicket() {
        String ticket = UUID.randomUUID().toString();
        return ticket.replaceAll("-", "");
    }

    /**
     * 短8位UUID
     *
     * @return
     */
    public static String shortUuid() {
        //调用Java提供的生成随机字符串的对象：32位，十六进制，中间包含-
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //分为8组
        for (int i = 0; i < 8; i++) {
            //每组4位
            String str = uuid.substring(i * 4, i * 4 + 4);
            //将4位str转化为int 16进制下的表示
            int x = Integer.parseInt(str, 16);

            //用该16进制数取模62（十六进制表示为314（14即E）），结果作为索引取出字符
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

}
