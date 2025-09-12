package com.luopc.platform.cloud.common.util;

import com.luopc.platform.common.core.util.SequenceIdUtil;
import com.luopc.platform.common.core.util.SmartNumIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SmartNumIDUtilTest {

    @Test
    void testNextPkId() throws InterruptedException {
        //for (int i = 0; i < 10; i++) {
        log.info(".nextPkId = {}", SmartNumIDUtil.nextPkId());
        //}
//        for (int i = 0; i < 9999; i++) {
        log.info("1.shortId = {}", SmartNumIDUtil.shortId());
//        }
        log.info("2.shortId = {}", SequenceIdUtil.shortId());
        log.info("3. nextId = {}", SequenceIdUtil.nextId());
    }

}
