package com.whoz_in.main_api.query.shared.presentation;

import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeFormatter {

    // "N시간 M분" 또는 "M분" 형식으로 변환
    public static String toHourMinute(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        } else {
            return String.format("%d분", minutes);
        }
    }

    // "N시간" 형식으로 변환
    public static String toHour(Duration duration) {
        long hours = duration.toHours();
        return String.format("%d시간", hours);
    }
}
