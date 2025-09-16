package com.luopc.platform.web.exception.log;

import com.luopc.platform.web.util.HttpClientPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AppErrorCodeLoggerTest {
    private static final AppErrorCodeLogger LOGGER = AppErrorCodeLoggerFactory.getLogger(AppErrorCodeLoggerTest.class);

    @Test
    public void testErrorMessage() {
        System.out.println(AppErrorCodeLogger.errorMessage("APP-100-400", "demo@example.com"));
    }

    @Test
    public void testPrintErrorMessage() {
        log.info("APP-100-400", "demo@example.com");
        LOGGER.error("APP-100-400", "demo@example.com");
    }

    @Test
    public void testMessageFormat() {
        final FormattingTuple tuple = MessageFormatter.format("Hi {}", "good");
        System.out.println(tuple.getMessage());

        String testStr = "promscrape003Aconfig003A";

        Matcher matcher= Pattern.compile("00[A-Za-z0-9]{2}").matcher(testStr);
        if(matcher.find()){
            System.out.println("-------------------------------------------");
            System.out.println(matcher.groupCount());
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String sourceStr = matcher.group(i);
                System.out.println("sourceStr=" + sourceStr);
                String encodeStr = sourceStr.replace("00", "%");
                System.out.println("encodeStr=" + encodeStr);
                String decodeStr = URLDecoder.decode(encodeStr);
                System.out.println("decodeStr=" +decodeStr);

                String result = testStr.replace(sourceStr, decodeStr);
                System.out.println("result="+result);
            }
            System.out.println("-------------------------------------------");
        }

        System.out.println(URLEncoder.encode("="));
        System.out.println(URLDecoder.decode("%3A"));
    }

    @Test
    public void testPort(){
        for (int i = 1; i <= 200; i++) {
            System.out.println(HttpClientPortUtil.randomPort());
        }
        log.info("Test print 中文： {}", "This is Chinese words - 这是一行中文。");

        System.out.println((int)'n');

    }
}
