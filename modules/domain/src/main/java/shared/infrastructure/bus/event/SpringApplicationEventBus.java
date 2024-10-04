package shared.infrastructure.bus.event;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import shared.domain.bus.event.DomainEvent;
import shared.domain.bus.event.EventBus;


/*
기본적인 이벤트 퍼블리셔
추후에 카프카나 토끼MQ를 공부하면 새로운 구현체를 만들어봅시다.
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
