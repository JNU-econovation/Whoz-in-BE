package com.whoz_in.shared;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DayEndedEventPublisher {
    // 후즈인에서 새로운 하루의 시작을 나타내는 시각
    public static final int DAY_END_HOUR = 6;
    private static final String CRON = "0 0 " + DAY_END_HOUR + " * * *";

    private final ApplicationEventPublisher publisher;

    @Scheduled(cron = CRON)
    public void publishDayEndedEvent() {
        LocalDateTime now = LocalDateTime.now().with(LocalTime.of(DAY_END_HOUR, 0));
        log.info("[DayEnded] 이벤트 발행: {}", now);
        publisher.publishEvent(new DayEnded(now));
    }
}
