package com.whoz_in.main_api.query.shared.presentation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;

public class DurationToHourMinuteSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        String formatted;

        if (hours > 0) {
            formatted = String.format("%d시간 %d분", hours, minutes);
        } else {
            formatted = String.format("%d분", minutes);
        }

        gen.writeString(formatted);
    }
}
