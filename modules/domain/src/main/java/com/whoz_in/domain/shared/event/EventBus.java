package com.whoz_in.domain.shared.event;

import java.util.List;

public interface EventBus {
    void publish(List<DomainEvent> events);
}
