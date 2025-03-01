package com.whoz_in.main_api.shared.event.fail;

import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.shared.event.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FailedEventRetryer {

    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 1000 * 30)
    public void retry() {
        FailedEventStore.poll().forEach(event -> {
            log.info("[이벤트 재시도] {} ", event.getClass().getName());
            eventPublisher.publishEvent(event);
        });
    }

}
