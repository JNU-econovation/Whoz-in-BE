package com.whoz_in.domain.shared.domain.bus.event;

import java.util.List;

//DomainEvent
public interface EventBus {
    void publish(List<DomainEvent> events);
}
