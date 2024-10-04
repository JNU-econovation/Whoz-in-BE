package shared.domain.bus.event;

import java.time.LocalDateTime;
import lombok.Getter;

/*
비즈니스 로직에서 발생하는 이벤트를 뜻한다.
EventBus를
 */
@Getter
public abstract class DomainEvent {
    private final LocalDateTime occurredOn;

    protected DomainEvent() {
        this.occurredOn = LocalDateTime.now();
    }
}
