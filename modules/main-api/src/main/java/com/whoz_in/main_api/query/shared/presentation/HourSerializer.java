package com.whoz_in.main_api.query.shared.presentation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;

public class HourSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        long hours = duration.toHours();
        String formatted = String.format("%d시간", hours);
        gen.writeString(formatted);
    }
}
