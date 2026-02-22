package com.luopc.platform.web.common.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequenceIdUtilTest {

    @Test
    void shortId() {
        for (int i = 0; i < 100; i++) {
            System.out.println(SequenceIdUtil.shortId());
        }
    }
}
