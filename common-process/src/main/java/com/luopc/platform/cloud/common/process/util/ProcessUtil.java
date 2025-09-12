package com.luopc.platform.cloud.common.process.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ProcessUtil {

    public static String getRealPathByFileName(String fileName, URL sourcePath) {
        File exeFile = new File(fileName);
        if (exeFile.exists()) {
            return fileName;
        } else {
            //URL sourcePath = this.getClass().getClassLoader().getResource(fileName);
            if (sourcePath != null) {
                exeFile = new File(sourcePath.getPath());
                if (exeFile.exists()) {
                    return sourcePath.getPath();
                }
            } else {
                String canonicalPath = null;
                try {
                    File directory = new File("");
                    canonicalPath = directory.getCanonicalPath();
                } catch (IOException e) {
                    log.error("Unable to get root path");
                }
                String rootPath = canonicalPath + File.separator + fileName;
                exeFile = new File(rootPath);
                if (exeFile.exists()) {
                    return rootPath;
                }
            }
        }
        return null;
    }

    public static String decode(String sourceValue) {
        //003A=%3A=:
        sourceValue = decodeByPattern(sourceValue, "00[A-Za-z0-9]{2}");
        return sourceValue;
    }

    public static String decodeByPattern(String sourceValue, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(sourceValue);
        if (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String sourceStr = matcher.group(i);
                String encodeStr = sourceStr.replace("00", "%");
                String decodeStr = URLDecoder.decode(encodeStr);

                sourceValue = sourceValue.replace(sourceStr, decodeStr);
                log.info("convert sourceStr[{}] to encodeStr[{}] and decode to [{}], result value={}", sourceStr, encodeStr, decodeStr, sourceValue);
            }
        }
        return sourceValue;
    }


    public static List<Long> getPidList(Process process) {
        List<Long> pidList = new ArrayList<>();
        Stream<ProcessHandle> stream = process.descendants();
        List<ProcessHandle> list = stream.collect(Collectors.toList());
        for (ProcessHandle processHandle : list) {
            pidList.add(processHandle.pid());
        }
        pidList.add(process.pid());
        return pidList;
    }


    public static String getLocalCharSet() {
        String os = System.getProperty("os.name");
        String charSet;
        if (os.toLowerCase().contains("win")) {
            charSet = "gbk";
        } else {
            charSet = "utf-8";
        }
        return charSet;
    }

    public static String getStopCommand(Long pid) {
        String os = System.getProperty("os.name");
        String command;
        if (os.toLowerCase().contains("win")) {
            command = "Taskkill /pid " + pid;
        } else {
            command = "kill -s 15 " + pid;
        }
        return command;
    }

    public static String getKillCommand(Long pid) {
        String os = System.getProperty("os.name");
        String command;
        if (os.toLowerCase().contains("win")) {
            command = "taskkill/F /pid " + pid;
        } else {
            command = "kill -s 9 " + pid;
        }
        return command;
    }
}
