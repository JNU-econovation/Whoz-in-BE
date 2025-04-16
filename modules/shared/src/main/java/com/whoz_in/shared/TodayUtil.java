package com.whoz_in.shared;

import static com.whoz_in.shared.DayEndedEventPublisher.DAY_END_HOUR;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 특정 시각(LocalDateTime)을 {@link com.whoz_in.shared.DayEndedEventPublisher#DAY_END_HOUR} 기준으로 날짜로 환산합니다. <br>
 * - 해당 시각이 {@code DAY_END_HOUR} 이전이면, 해당 날짜는 '전날'로 간주됩니다. <br>
 * - 해당 시각이 {@code DAY_END_HOUR} 이후면, 해당 날짜는 '당일'로 간주됩니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodayUtil {
    public static LocalDate today(){
        return LocalDateTime.now().minusHours(DAY_END_HOUR).toLocalDate();
    }

    public static LocalDate someday(LocalDateTime at){
        return at.minusHours(DAY_END_HOUR).toLocalDate();
    }
}
