package com.whoz_in.domain.shared.event;

import com.whoz_in.shared.domain_event.DomainEvent;
import java.util.List;

public interface EventBus {
    void publish(List<DomainEvent> events);
}
