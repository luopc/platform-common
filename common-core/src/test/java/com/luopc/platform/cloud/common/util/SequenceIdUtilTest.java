package com.luopc.platform.cloud.common.util;

import com.luopc.platform.common.core.util.GeneratorUtil;
import com.luopc.platform.common.core.util.SequenceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SequenceIdUtilTest {

    @Test
    void nextId() throws InterruptedException {
//        for (int i = 0; i < 20; i++) {
//            log.info("Next SequenceId = {}", SequenceIdUtil.nextId());
//        }
        long seqId = SequenceIdUtil.shortId();
        seqId = SequenceIdUtil.shortId();
        log.info("first GenerateShortId = {}", seqId);

        long workId = SequenceIdUtil.getMaxWorkerId(99);
        log.info("workId = {}", workId);
//        for (int i = 0; i < 61; i++) {
            long seqId1 = SequenceIdUtil.shortId();
//            long seqId2 = SequenceIdUtil.nextId();
            log.info("GenerateShortId = {}", seqId1);

//            log.info("Next SequenceId = {}", seqId2);
//        }
//        long seqId2 = SequenceIdUtil.generateShortId();
//        log.info("latest GenerateShortId = {}", seqId2);
//        Thread.sleep(60 * 1000);
//        long seqId1 = SequenceIdUtil.generateShortId();
//        log.info("Next GenerateShortId = {}", seqId1);
//        Thread.sleep(60 * 1000);
//        long seqId2 = SequenceIdUtil.generateShortId();
//        log.info("Next GenerateShortId = {}", seqId2);
    }
    //first  GenerateShortId = 240252 53000
    //latest GenerateShortId = 240252 981034

    @Test
    void testUuid(){
        String uuid = GeneratorUtil.shortUuid();
        log.info("GenerateShortId = {}", uuid);
        String longTick = GeneratorUtil.longTicket();
        log.info("GenerateShortId = {}", longTick);
        String shortTick = GeneratorUtil.shortTicket();
        log.info("GenerateShortId = {}", shortTick);
    }
}
