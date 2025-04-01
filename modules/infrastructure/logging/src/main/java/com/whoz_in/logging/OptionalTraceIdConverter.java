package com.whoz_in.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

public class OptionalTraceIdConverter extends CompositeConverter<ILoggingEvent> {

    @Override
    protected String transform(ILoggingEvent event, String in) {
        String traceId = event.getMDCPropertyMap().get("traceId");
        return (traceId != null && !traceId.isEmpty()) ? "[" + traceId + "]" : "";
    }
}
