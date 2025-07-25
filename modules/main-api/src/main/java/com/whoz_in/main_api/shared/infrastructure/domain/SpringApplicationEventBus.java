package com.whoz_in.main_api.shared.infrastructure.domain;

import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


/*
기본적인 이벤트 퍼블리셔
추후에 카프카나 Rabbit MQ를 공부하면 새로운 구현체를 만들어봅시다.
 */
@Service
@RequiredArgsConstructor
public final class SpringApplicationEventBus implements EventBus {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this.publisher::publishEvent);
    }
}
