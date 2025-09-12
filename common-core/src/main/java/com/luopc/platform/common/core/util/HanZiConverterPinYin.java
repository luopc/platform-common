package com.luopc.platform.common.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin
 */
@Slf4j
public class HanZiConverterPinYin {

    public HanZiConverterPinYin() {
        // 初始化汉字拼音字母对照表
        initChinesePinyinComparisonMapList();
    }

    /**
     * 汉字拼音字母对照表
     */
    private List<ChinesePinyinComparisonMap> chinesePinyinComparisonMapList;

    /**
     * 初始化汉字拼音字母对照表
     */
    private void initChinesePinyinComparisonMapList() {
        chinesePinyinComparisonMapList = new ArrayList<ChinesePinyinComparisonMap>();

        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-20319, -20284, 'A'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-20283, -19776, 'B'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-19775, -19219, 'C'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-19218, -18711, 'D'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-18710, -18527, 'E'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-18526, -18240, 'F'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-18239, -17923, 'G'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-17922, -17418, 'H'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-17417, -16475, 'J'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-16474, -16213, 'K'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-16212, -15641, 'L'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-15640, -15166, 'M'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-15165, -14923, 'N'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-14922, -14915, 'O'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-14914, -14631, 'P'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-14630, -14150, 'Q'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-14149, -14091, 'R'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-14090, -13319, 'S'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-13318, -12839, 'T'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-12838, -12557, 'W'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-12556, -11848, 'X'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-11847, -11056, 'Y'));
        chinesePinyinComparisonMapList.add(new ChinesePinyinComparisonMap(-11055, -10247, 'Z'));
    }

    /**
     * 遍历获取首字母
     */
    public char getPY(char c) throws Exception {
        byte[] bytes = String.valueOf(c).getBytes("GBK");

        //双字节汉字处理
        if (bytes.length == 2) {

            int hightByte = 256 + bytes[0];
            int lowByte = 256 + bytes[1];
            int asc = (256 * hightByte + lowByte) - 256 * 256;

            // 遍历转换
            for (ChinesePinyinComparisonMap map : this.chinesePinyinComparisonMapList) {
                if (asc >= map.getSAscll() && asc <= map.getEAscll()) {
                    return map.getCode();
                }
            }
        }

        // 单字节或其他直接输入，不执行编码
        //return c;
        //单字节或其他直接输入，随机生成一个字母
//        return (char) (int) (Math.random() * 26 + 65);
        return 'X';
    }

    /**
     * 获取汉字拼音
     */
    public String getHanZiPinYin(String str) {
        try {
            StringBuilder pyStrBd = new StringBuilder();
            for (char c : str.toCharArray()) {
                pyStrBd.append(getPY(c));
            }
            return pyStrBd.toString();
        } catch (Exception e) {
            log.error("Cannot get code for str[{}]", str, e);
        }
        return null;
    }


    /**
     * 汉字拼音字母对照类
     */
    @Data
    @AllArgsConstructor
    private static class ChinesePinyinComparisonMap {
        // 区间开头
        private int sAscll;

        // 区间结尾
        private int eAscll;

        // 对应字母
        private char code;
    }

    public static void main(String[] args) {
        HanZiConverterPinYin hanZiConverterPinYin = new HanZiConverterPinYin();
        String code = hanZiConverterPinYin.getHanZiPinYin("为了 配合 模糊 查询 很多 情况下 我们 需要 用到 汉字 首字母 进行 模糊 查询， 这样 的 例子 很多， 做法 也很多");
        System.out.println(code);
    }
}
