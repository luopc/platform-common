package com.luopc.platform.web.common.core.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackStringConverter extends ClassicConverter {

    private final String randomTicket = SimpleTicketGeneratorUtil.shortUuid();

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return randomTicket;
    }
}
