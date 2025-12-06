package com.luopc.platform.cloud.common.util;

import com.luopc.platform.web.common.core.util.SequenceIdUtil;
import com.luopc.platform.web.common.core.util.SimpleNumIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SimpleNumIDUtilTest {

    @Test
    void testNextPkId() throws InterruptedException {
        //for (int i = 0; i < 10; i++) {
        log.info(".nextPkId = {}", SimpleNumIDUtil.nextPkId());
        //}
//        for (int i = 0; i < 9999; i++) {
        log.info("1.shortId = {}", SimpleNumIDUtil.shortId());
//        }
        log.info("2.shortId = {}", SequenceIdUtil.shortId());
        log.info("3. nextId = {}", SequenceIdUtil.nextId());
    }

}
