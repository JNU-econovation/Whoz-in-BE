package com.whoz_in.domain.shared.domain.bus.event;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class DomainEvent {
    private final LocalDateTime occurredOn;

    protected DomainEvent() {
        this.occurredOn = LocalDateTime.now();
    }
}
